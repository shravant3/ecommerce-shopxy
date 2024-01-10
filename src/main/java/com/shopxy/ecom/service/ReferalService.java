package com.shopxy.ecom.service;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.shopxy.ecom.repository.ReferalRepository;
import com.shopxy.ecom.model.Refferal;
import com.shopxy.ecom.model.User;

@Service
public class ReferalService {

    @Autowired
    private ReferalRepository referalRepository;

    private static long murmur3Hash(String input) {
        // Basic hash function for educational purposes
        return input.hashCode();
    }

    public String generateShortReferralId(User user) {
        ObjectId objectId = ObjectId.get();
        String hexString = objectId.toString(); // Use toString() instead of toHexString()
        
        long hash = murmur3Hash(hexString);
        hash = Math.abs(hash);

        return Long.toString(hash);
    }

    public void CreateReferral(User user) {
        String shortReferralId = generateShortReferralId(user);

        Refferal referal = new Refferal();
        referal.setUser(user);
        referal.setReferalCode(shortReferralId);
        referalRepository.save(referal);
    }

    public Refferal findByUser(User user) {
        return referalRepository.findByUser(user);
    }

    public Boolean UseRefferalByCode(String referralCode) {
        Refferal referral = referalRepository.findByReferalCode(referralCode);
        if (referral.getReferalCount() < 2) {
            referral.setReferalCount(referral.getReferalCount() + 1);
            return true;
        }
        return false;
    }

    public User getUserBycode(String referralCode) {
        Refferal referral = referalRepository.findByReferalCode(referralCode);
        return referral.getUser();
    }

    public Boolean getValidcode(String referralCode) {
        return referalRepository.existsByReferalCode(referralCode);
    }
}
