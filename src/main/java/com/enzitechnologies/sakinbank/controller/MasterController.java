package com.enzitechnologies.sakinbank.controller;


import com.enzitechnologies.sakinbank.model.Account;
import com.enzitechnologies.sakinbank.model.Loan;
import com.enzitechnologies.sakinbank.service.AccountService;
import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.PrecheckStatusException;
import com.hedera.hashgraph.sdk.ReceiptStatusException;
import com.hedera.hashgraph.sdk.TransactionId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeoutException;

@Controller
public class MasterController {

    @Autowired
    private AccountService accountService;

    @RequestMapping("/")
    public String showLoginPage(){

        return "redirect:/loginPage";
    }

    @GetMapping("/loginPage")
    public String loginPage(){
        return "login";
    }

    @GetMapping("/registerPage")
    public String registerPage(){

        return "register";
    }

    @PostMapping("/login")
    public String login(@RequestParam("username") String username,
                        Model model) throws TimeoutException, PrecheckStatusException {

        Account account = accountService.getAccountByName(username);

        if (account != null){

            model.addAttribute("account", account);

            accountService.getAccountBalance(account.getAccountId());

            return "index";
        }

        return "redirect:/loginPage?login_failed";
    }

    @GetMapping("/logout")
    public String logout(){

        return "redirect:/loginPage?logged_out";

    }

    @GetMapping("/index")
    public String showIndex(@RequestParam("accountId") String accountId,  Model model) throws TimeoutException, PrecheckStatusException {


        model.addAttribute("account", accountService.getAccountById(accountId));
        model.addAttribute("bal", accountService.getAccountBalance(accountId));
        return "index";
    }

    @GetMapping("/loadHbarPage")
    public String loadHbarPage(
            @RequestParam("accountId") String accountId,
            Model model){
                 model.addAttribute("account", accountService.getAccountById(accountId));

        return "load_hbar";
    }

    @PostMapping("/register")
    public String register(@RequestParam("username") String username) throws ReceiptStatusException, PrecheckStatusException, TimeoutException {

        if (accountService.createAccount(username).isPresent()){
            return "redirect:/loginPage?registration_successful";
        }

        return "redirect:/registerPage?registration_error";
    }

    @GetMapping("/makeDepositPage")
    public String depositPage(@RequestParam("accountId") String accountId, Model model){

        model.addAttribute("account", accountService.getAccountById(accountId));

        return "make_deposit";

    }

    @PostMapping("/deposit/{accountId}")
    public String makeDeposit(@PathVariable("accountId") String accountId,
                            @RequestParam("amount") long amount, Model model) throws PrecheckStatusException, ReceiptStatusException, TimeoutException {

        Account depositor = accountService.getAccountById(accountId);

        if(depositor != null) {
            accountService.makeDeposit(depositor, amount);
            model.addAttribute("account", depositor);

            return "index";
        }

        return null;
    }

    @PostMapping("/withdraw")
    public void withdraw(@RequestParam("sender_id") String sender_id,
                         @RequestParam("recipient_id") String recipient_id,
                         @RequestParam("amount") long amount) throws PrecheckStatusException, ReceiptStatusException, TimeoutException {
        Account sender = accountService.getAccountById(sender_id);

        if (amount > 0){
            TransactionId transactionId =  accountService.withdrawAmount(Objects.requireNonNull(sender), AccountId.fromString(Objects.requireNonNull(recipient_id)), amount);

            assert transactionId.accountId != null;
        }
    }

    @GetMapping("/loansPage")
    public String showLoansPage(@RequestParam("accountId") String accountId, Model model){

        Account account = accountService.getAccountById(accountId);

        List<Loan> loans = account.getLoans();

        model.addAttribute("account", account);

        model.addAttribute("loans", loans);

        return "loans_page";

    }

    @GetMapping("/borrowLoanPage")
    public String borrowLoanPage(@RequestParam("account_id") String account_id, Model model){
        Account borrower = accountService.getAccountById(account_id);

        double max_principle = accountService.getAccumulatedSSP(borrower) * borrower.getAccount_balance();

        model.addAttribute("max_principle", max_principle);

        return "borrow_loan";
    }

    @PostMapping("/borrowLoan")
    public void borrowLoan(@RequestParam("account_id") String account_id,
                           @RequestParam("amount") long amount) throws PrecheckStatusException, ReceiptStatusException, TimeoutException {
        Account borrower = accountService.getAccountById(account_id);

        accountService.borrowLoan(borrower, amount);
    }

    @PostMapping("/repayLoanPage")
    public String showRepayLoanPage(
            @RequestParam("account_id") String account_id,
            @RequestParam("loan_id") String loan_id,
            Model model){

        Account account = accountService.getAccountById(account_id);

        model.addAttribute("account", account);


        return "borrow_loan";
    }

    @GetMapping("/savingsPointsPage")
    public String showSSPPage(@RequestParam("accountId") String accountId, Model model){
        model.addAttribute("account", accountService.getAccountById(accountId));


        return "ssp_page";
    }

    @GetMapping("/transferSSPPage")
    public String transferSSPPage(@RequestParam("accountId") String accountId, Model model){

        Iterable<Account> all_accounts = accountService.getAllAccounts();

        Account sender_account = accountService.getAccountById(accountId);

        model.addAttribute("sender_id", sender_account.getAccountId());

        model.addAttribute("account", sender_account);

        model.addAttribute("all_accounts", all_accounts);

        return "transfer_ssp";

    }

    @PostMapping("/transferSSP/{sender_id}")
    public String transferSSP(@PathVariable("sender_id") String sender_id,
                            @RequestParam("recipient_id") String recipient_id,
                            @RequestParam("amount") long amount,
                              Model model) throws PrecheckStatusException, ReceiptStatusException, TimeoutException {

        Account sender = accountService.getAccountById(sender_id);
        Account recipient = accountService.getAccountById(recipient_id);

        if ((amount > 0) && (amount <= sender.getSSP_Balance())) {

            TransactionId transactionId = accountService.shareSSP(recipient, sender, amount);

            assert transactionId.accountId != null;

            accountService.getAccountBalance(sender_id);

            model.addAttribute("account", sender);

            return "index";


        }else
            return null;
    }

}
