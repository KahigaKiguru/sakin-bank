package com.enzitechnologies.sakinbank.service;

import com.enzitechnologies.sakinbank.model.Account;
import com.enzitechnologies.sakinbank.model.Deposit;
import com.enzitechnologies.sakinbank.model.Loan;
import com.enzitechnologies.sakinbank.model.Token;
import com.enzitechnologies.sakinbank.repository.AccountRepository;
import com.hedera.hashgraph.sdk.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.TimeoutException;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private DepositService depositService;


    public AccountService() throws PrecheckStatusException, ReceiptStatusException, TimeoutException {
    }

    //    create account
    public Optional<Account> createAccount(String user_name) throws TimeoutException, PrecheckStatusException, ReceiptStatusException {

        Account treasury = accountRepository.findById("0.0.14410").get();

//        Generate client to handle transactions
            Client client = Client
                .forTestnet()
                .setOperator(
                        AccountId.fromString(treasury.getAccountId()),
                        PrivateKey.fromString(treasury.getPrivate_key()));

//        Generate new Account's key pair
        PrivateKey privateKey = PrivateKey.generate();
        PublicKey publicKey= privateKey.getPublicKey();

//        Create Hedera Testnet Account
        TransactionReceipt receipt= new AccountCreateTransaction()
                .setKey(publicKey)
                .setInitialBalance(Hbar.fromTinybars(0))
                .execute(client).getReceipt(client);

//        Extract accountId from receipt
        AccountId accountId = receipt.accountId;

//        Get new Account Info

        assert accountId != null;

        AccountInfo accountInfo = new AccountInfoQuery()
                .setAccountId(accountId)
                .execute(client);

//        Associate account with the SSP token

        boolean isAssociated = false;

        if (associateTokenWithAccount(accountInfo.accountId, privateKey) == Status.SUCCESS){
            isAssociated = true;
        }


//        save account to database
        accountRepository.save(
                new Account(
                accountInfo.accountId.toString(),
                user_name,
                privateKey.toString(),
                accountInfo.key.toString(),
                accountInfo.balance.toTinybars(),
                accountInfo.isDeleted,
                        isAssociated,
                accountInfo.expirationTime,
                accountInfo.autoRenewPeriod,
                        null,
                        null)
        );

        return accountRepository.findById(accountInfo.accountId.toString());

    }

//    transfer hbar
    private TransactionId transferHbar(Account recipient, Account sender, Hbar amount) throws TimeoutException, PrecheckStatusException, ReceiptStatusException {

//        Create the sender's client to handle transfers
        Client client = Client.forTestnet().setOperator(AccountId.fromString(sender.getAccountId()), PrivateKey.fromString(sender.getPrivate_key()));

//        Transfer hbar
        TransactionRecord transferRecord =  new TransferTransaction()
                .addHbarTransfer(AccountId.fromString(sender.getAccountId()),amount.negated())
                .addHbarTransfer(AccountId.fromString(recipient.getAccountId()), amount)
                .execute(client)
                .getRecord(client);

        return  transferRecord.transactionId;

    }

//    transfer SSPq
    private TransactionId transferSSP(Account recipient, Account sender, long amount) throws TimeoutException, PrecheckStatusException, ReceiptStatusException {

//        Create the sender's client to handle transfers
        Client client = Client.forTestnet().setOperator(AccountId.fromString(sender.getAccountId()), PrivateKey.fromString(sender.getPrivate_key()));

        Token SSP = tokenService.getToken();

//        Create a Hedera Token Transfer between sender and recipient
        TransactionRecord sspTransferRecord = new TransferTransaction()
                .addTokenTransfer(TokenId.fromString(tokenService.getToken().getTokenId()), AccountId.fromString(sender.getAccountId()), (amount * - 1))
                .addTokenTransfer(TokenId.fromString(tokenService.getToken().getTokenId()), AccountId.fromString(recipient.getAccountId()), amount)
                .execute(client)
                .getRecord(client);

//        get sender's SSP Token Balance
        Map<TokenId, Long> sender_record = new AccountBalanceQuery()
                .setAccountId(AccountId.fromString(sender.getAccountId()))
                .execute(client)
                .token;

        long sender_ssp_bal = sender_record.get(TokenId.fromString(tokenService.getToken().getTokenId()));

//        update sender's SSP balance
        sender.setSSP_Balance(sender_ssp_bal);

//        update sender's record
        accountRepository.save(sender);

//        get recipient's SSP Token Balance
        Map<TokenId, Long> recipient_record = new AccountBalanceQuery()
                .setAccountId(AccountId.fromString(sender.getAccountId()))
                .execute(client)
                .token;

        long recipient_ssp_bal = recipient_record.get(TokenId.fromString(tokenService.getToken().getTokenId()));

//        update sender's SSP balance
        recipient.setSSP_Balance(recipient_ssp_bal);

//        update sender's record
        accountRepository.save(recipient);

        return sspTransferRecord.transactionId;

    }

//    Associate account with token
    private Status associateTokenWithAccount(AccountId accountId, PrivateKey privateKey) throws TimeoutException, PrecheckStatusException, ReceiptStatusException {

        Account treasury = accountRepository.findById("0.0.14410").get();

        Client client = Client.forTestnet().setOperator(AccountId.fromString(treasury.getAccountId()), PrivateKey.fromString(treasury.getPrivate_key()));

        Token SSP = tokenService.getToken();

        List<TokenId> tokenIds = new ArrayList<>();

        tokenIds.add(TokenId.fromString(SSP.getTokenId()));

    TransactionReceipt tk_associate_receipt = new TokenAssociateTransaction()
            .setAccountId(accountId)
            .setTokenIds(tokenIds)
            .freezeWith(client)
            .sign(privateKey)
            .sign(PrivateKey.fromString(treasury.getPrivate_key()))
            .execute(client)
            .getReceipt(client);

    return tk_associate_receipt.status;
}


//  get account by username
    public Account getAccountByName(String username){

        return accountRepository.findByUsername(username);

    }

//    get account by AccountId (the account's ID)
    public Account getAccountById(String accountID){
        if(accountRepository.findById(accountID).isPresent()) {
            return accountRepository.findById(accountID).get();
        }
        return null;
    }

//    get all accounts
    public Iterable<Account> getAllAccounts(){
        return accountRepository.findAll();
    }

//  make a deposit
    public void makeDeposit(Account depositor, long amount) throws ReceiptStatusException, PrecheckStatusException, TimeoutException {

        Client client = Client
                .forTestnet()
                .setOperator(
                        AccountId
                                .fromString(depositor.getAccountId()),
                        PrivateKey
                                .fromString(depositor.getPrivate_key()));

        Account sakin = accountRepository.findById("0.0.14410").get();

//        Transfer hbar from depositor to sakin
        TransactionId transferId = transferHbar(sakin, depositor, Hbar.from(amount));

//        Get depositor's account balance from Hedera
        Hbar depositor_balance = new AccountBalanceQuery()
                .setAccountId(AccountId.fromString(depositor.getAccountId()))
                .execute(client)
                .hbars;

        depositor.setAccount_balance(depositor_balance.toTinybars() / 100000000);

        accountRepository.save(depositor);

//      Get the Sakin Account balance
        Hbar sakin_balance = new AccountBalanceQuery()
                .setAccountId(AccountId.fromString(sakin.getAccountId()))
                .execute(client)
                .hbars;

//        update sakin account balance
        sakin.setAccount_balance(sakin_balance.toTinybars() / 100000000);

//        update database
        accountRepository.save(sakin);

//        transfer Sakin Savings Points to the depositor
        transferSSP(depositor, sakin, getSSPEarned(amount));

        if (transferId != null){
//          Instantiate a deposit object to keep a record record of the deposit
            Deposit deposit = new Deposit(
                    transferId.toString(),
                    depositor.getAccountId(),
                    "pre-savings",
                    amount,
                    getSSPEarned(amount),
                    Date.from(Instant.now()),
                    depositor
            );

//            Add deposit to the depositor's record
            depositor.getDeposits().add(deposit);

//            persist deposit to database
            depositService.saveDeposit(deposit);

        }

    }

//  withdraw amount
    public TransactionId withdrawAmount(Account sender, AccountId recipient_id, long amount) throws ReceiptStatusException, PrecheckStatusException, TimeoutException {

        Client client = Client
                .forTestnet()
                .setOperator(
                        AccountId
                                .fromString(sender.getAccountId()),
                        PrivateKey
                                .fromString(sender.getPrivate_key()));

        Account recipient = new Account(
                recipient_id.toString(),
                "withdraw_recipient",
                null,
                null,
                0,
                false,
                false,
                null,
                null,
                null,
                null

        );


        TransactionId transferId =  transferHbar(recipient, sender, Hbar.from(amount));

//        Query Hedera for the sender's account balance
        Hbar sender_balance = new AccountBalanceQuery()
                .setAccountId(AccountId.fromString(sender.getAccountId()))
                .execute(client)
                .hbars;

//        update sender's account balance
        sender.setAccount_balance(sender_balance.toTinybars());

//        update sender's record in the database
        accountRepository.save(sender);

//        Query Hedera for the recipient's account balance
        Hbar recipient_balance = new AccountBalanceQuery()
                .setAccountId(AccountId.fromString(recipient.getAccountId()))
                .execute(client)
                .hbars;

//      update recipient's account balance
        recipient.setAccount_balance(recipient_balance.toTinybars());

//        update recipient's record in the database
        accountRepository.save(recipient);

        return transferId;

    }

//  share SSP with other Sakin users
    public TransactionId shareSSP(Account sender, Account recipient, long amount) throws ReceiptStatusException, PrecheckStatusException, TimeoutException {
        return  transferSSP(sender, recipient, amount);
    }

//  borrow a loan
    public void borrowLoan(Account borrower, long amount) throws ReceiptStatusException, PrecheckStatusException, TimeoutException{

        Account sakin = accountRepository.findById("0.0.14410").get();

//        get accumulated SSPs
        long ssp_total = getAccumulatedSSP(borrower);

        double max_principle = borrower.getAccount_balance() * ssp_total;

        long ssps_needed = getSSPEarned(amount);

        Loan newLoan = new Loan(
                borrower,
                ssps_needed,
                amount,
                0,
                Date.from(Instant.now()),
                0
        );

        if(amount <= max_principle){
            borrower.getLoans().add(newLoan);
        }

//        transfer borrower's ssp to sakin
        transferSSP(sakin, borrower, ssps_needed);

//        transfer hbar to borrower's wallet
        transferHbar(borrower, sakin, Hbar.from(amount));

    }

//  repay a loan
    public void repayLoan(String account_id, String loan_id, long amount){
        Account account = accountRepository.findById(account_id).get();

        List<Loan> loans = account.getLoans();

        Loan active_loan = null;

        for (Loan loan: loans) {
            if (loan.getLoan_id().equals(loan_id)){
                active_loan = loan;
                break;
            }
            break;
        }


    }
//    calculate SSPs per Savings Deposits
    private long getSSPEarned(long deposit_amount){

//        ssp earned = 90% of the deposit amount * a savings factor of 0.7
        return (long) ((deposit_amount * 0.9) * 0.7);

    }

//    get accumulated SSPs
    public long getAccumulatedSSP(Account account){
        long total_ssp = 0;

        for (Deposit dp: accountRepository.findById(account.getAccountId()).get().getDeposits()) {
            total_ssp += dp.getSsp_generated();
        }

        return  total_ssp;
    }

//    get account balance
    public long getAccountBalance(String accountId) throws TimeoutException, PrecheckStatusException {

        Account account = accountRepository.findById(accountId).get();

        Client client =
                Client
                        .forTestnet()
                        .setOperator(
                                AccountId
                                        .fromString(account.getAccountId()),
                                PrivateKey
                                        .fromString(account.getPrivate_key()));


        AccountBalance balance_query = new AccountBalanceQuery()
                .setAccountId(AccountId.fromString(account.getAccountId()))
                .execute(client);

        long hbar_balance = balance_query.hbars.toTinybars()/100000000;

        long ssp_balance = balance_query.token.get(TokenId.fromString(tokenService.getToken().getTokenId()));


        account.setAccount_balance(hbar_balance);

        account.setSSP_Balance(ssp_balance);

        accountRepository.save(account);

        return hbar_balance;
    }
}
