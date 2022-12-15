package library.service.implementation;

import library.model.Wallet;
import library.repository.WalletRepository;
import library.service.interfaces.WalletService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WalletServiceImp implements WalletService {

    private final static Logger logger = LoggerFactory.getLogger(WalletServiceImp.class);

    private final WalletRepository walletRepository;

    @Autowired
    public WalletServiceImp(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    @Override
    public boolean replenishmentFunds(Long incomingFunds, Long id) {
        try {
            Wallet wallet = walletRepository.findById(id).orElseThrow(Exception::new);
            wallet.replenishmentFunds(incomingFunds);
            walletRepository.save(wallet);
        } catch (Exception e) {
            logger.warn("Wallet [id:{}] replenishment by {} is failed", id, incomingFunds);
            return false;
        }
        logger.info("Wallet [id:{}] replenished by {}", id, incomingFunds);
        return true;
    }

    @Override
    public boolean debitingFunds(Long requiredFunds, Long id) {
        try {
            Wallet wallet = walletRepository.findById(id).orElseThrow(Exception::new);
            if (!wallet.debitingFunds(requiredFunds)) {
                logger.info("In Wallet [id:{}] not enough founds for debiting by {}. ", id, requiredFunds);
                return false;
            }
            walletRepository.save(wallet);
        } catch (Exception e) {
            logger.warn("Wallet [id:{}] replenishment by {} is failed. Cause: {}", id, requiredFunds, e.getMessage());
            return false;
        }
        logger.info("Wallet [id:{}] debiting by {}", id, requiredFunds);
        return true;
    }

    @Override
    public Long getBalance(Long id) {
        long balance;
        try {
            balance = walletRepository.findById(id).orElseThrow(Exception::new).getBalance();
        } catch (Exception e) {
            logger.warn("Wallet [id:{}] getting balance is failed. Cause: {}", id, e.getMessage());
            return null;
        }
        return balance;
    }
}
