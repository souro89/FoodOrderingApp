package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.CustomerDao;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.exception.SignUpRestrictedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class CustomerService {

    @Autowired
    private PasswordCryptographyProvider passwordCryptographyProvider;

    @Autowired
    private CustomerDao customerDao;

    public Object getCustomer(String database_accesstoken2) {
        return null;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public CustomerEntity saveCustomer(CustomerEntity customerEntity) throws SignUpRestrictedException {



        if(customerDao.checkContactNumber(customerEntity.getContactNumber())){
            throw new SignUpRestrictedException("SGR-001","This contact number is already registered! Try other contact number.");
        }

        if(customerEntity.getFirstName() == null || customerEntity.getFirstName()== ""
            || customerEntity.getContactNumber() == null || customerEntity.getContactNumber()==""
            || customerEntity.getEmail() == null || customerEntity.getEmail() == ""
            || customerEntity.getPassword() == null || customerEntity.getPassword() == ""
        ){
            throw new SignUpRestrictedException("SGR-005","Except last name all fields should be filled");
        }


        Pattern upperCase = Pattern.compile("[A-Z]");
        Matcher upperCaseCheck = upperCase.matcher(customerEntity.getPassword());

        Pattern oneDigit = Pattern.compile("[0-9]");
        Matcher oneDigitCheck = oneDigit.matcher(customerEntity.getPassword());

        Pattern specialCharacters = Pattern.compile("[#@$%&*!^]");
        Matcher specialCharactersCheck = specialCharacters.matcher(customerEntity.getPassword());

        if(customerEntity.getPassword().length()<8 || !upperCaseCheck.find() || !oneDigitCheck.find() || !specialCharactersCheck.find() ){
            throw new SignUpRestrictedException("SGR-004","Weak password!");
        }

        Pattern pattern = Pattern.compile("(^[A-Za-z0-9]+)@([A-Za-z0-9]+)(\\.)([A-Za-z0-9]+)");
        Matcher m = pattern.matcher(customerEntity.getEmail());
        System.out.println(m.find());


        if(!m.find()){
            throw new SignUpRestrictedException("SGR-002","Invalid email-id format!");
        }

        String[] encryptedText= passwordCryptographyProvider.encrypt(customerEntity.getPassword());
        customerEntity.setSalt(encryptedText[0]);
        customerEntity.setPassword(encryptedText[1]);

        return customerDao.saveCustomer(customerEntity);

    }

}
