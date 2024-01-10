package com.shopxy.ecom.service;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopxy.ecom.repository.WalletHistoryRepository;
import com.shopxy.ecom.repository.WalletRepository;
import com.shopxy.ecom.Util.WalletTransactionStatus;
import com.shopxy.ecom.model.User;
import com.shopxy.ecom.model.Wallet;
import com.shopxy.ecom.model.WalletHistory;

@Service
public class WalletService {
    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private WalletHistoryRepository walletHistoryRepository;

    DecimalFormat df = new DecimalFormat("#.00");

    public void AddToWallet(double totalAmount, User user) {
        Wallet wallet = walletRepository.findByUser(user);
        if (wallet == null) {
            wallet = new Wallet();
            wallet.setUser(user);
        }
        wallet.setCurrentBalance(Double.parseDouble(df.format(wallet.getCurrentBalance() + totalAmount)));
        walletRepository.save(wallet);
        createWalletHistory(totalAmount, WalletTransactionStatus.CREDITED, LocalDateTime.now(), wallet,"refund added");
    }

    public Wallet getWalletByUser(User user) {
        Wallet wallet = walletRepository.findByUser(user);
        if (wallet == null) {
            wallet = new Wallet();
            wallet.setUser(user);
            walletRepository.save(wallet);
        }
        return wallet;
    }

    public void referalAmount(User referer, User userdtls) {
        Wallet wallet1 = walletRepository.findByUser(referer);
        if (wallet1 == null) {
            wallet1 = new Wallet();
            wallet1.setUser(referer);
            wallet1.setCurrentBalance(100.0);
        } else {
            wallet1.setCurrentBalance(Double.parseDouble(df.format(wallet1.getCurrentBalance() + 100)));
        }
        walletRepository.save(wallet1);
        String message="referal amount is added";
        createWalletHistory(100, WalletTransactionStatus.CREDITED, LocalDateTime.now(), wallet1,message);
        Wallet wallet2 = new Wallet();
        wallet2.setCurrentBalance(100.0);
        wallet2.setUser(userdtls);
        walletRepository.save(wallet2);
        createWalletHistory(100, WalletTransactionStatus.CREDITED, LocalDateTime.now(), wallet2,message);
    }

    public void createWalletHistory(double amount,WalletTransactionStatus status,LocalDateTime datetime,Wallet wallet,String message)
    {
        WalletHistory walletHistory=new WalletHistory();
        walletHistory.setStatus(status);
        walletHistory.setTransactionTime(datetime);
        walletHistory.setWallet(wallet);
        walletHistory.setAmountTransferred(amount);
        walletHistory.setDescription(message);
        walletHistoryRepository.save(walletHistory);
    }

    public List<WalletHistory> findAllHistory(Wallet wallets) {

        for(WalletHistory walletHistory:walletHistoryRepository.findByWallet(wallets)){
            walletHistory.setAmountTransferred(Double.parseDouble(df.format(walletHistory.getAmountTransferred())));
            walletHistoryRepository.save(walletHistory);
        }
        return walletHistoryRepository.findByWallet(wallets);
    }

    public void debitedTransaction(double amount,Wallet wallets){
        WalletHistory walletHistory=new WalletHistory();
        walletHistory.setWallet(wallets);
        walletHistory.setAmountTransferred(amount);
        walletHistory.setDescription("payment for a product");
        walletHistory.setStatus(WalletTransactionStatus.DEBITED);
        walletHistory.setTransactionTime(LocalDateTime.now());
        System.out.println(walletHistory);
        walletHistoryRepository.save(walletHistory);
    }

    public void saveWallet(Wallet wallets) {
    
        walletRepository.save(wallets);
    }

}
