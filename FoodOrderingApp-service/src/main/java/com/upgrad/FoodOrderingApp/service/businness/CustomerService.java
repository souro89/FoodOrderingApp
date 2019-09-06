package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.CustomerDao;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAuthEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.exception.AuthenticationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.SignUpRestrictedException;
import com.upgrad.FoodOrderingApp.service.exception.UpdateCustomerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.security.auth.login.LoginException;
import java.time.ZonedDateTime;
import java.util.Base64;
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

    @Transactional(propagation = Propagation.REQUIRED)
    public CustomerAuthEntity loginService(String authorization) throws AuthenticationFailedException {
        byte[] decode = Base64.getDecoder().decode(authorization.split("Basic ")[1]);
        String decodedText = new String(decode);
        String[] decodedArray = decodedText.split(":");
        if(decodedArray.length<2){
            throw new AuthenticationFailedException("ATH-003","Incorrect format of decoded customer name and password");
        }
        if(decodedArray[0]==null || decodedArray[0]=="" ||decodedArray[1]==null || decodedArray[1]=="" ){
            throw new AuthenticationFailedException("ATH-003","Incorrect format of decoded customer name and password");
        }

        CustomerEntity customerEntity = customerDao.getCustomer(decodedArray[0]);

        if(customerEntity == null){
            throw new AuthenticationFailedException("ATH-001","This contact number has not been registered!");
        }

        String encryptedPassword = passwordCryptographyProvider.encrypt(decodedArray[1],customerEntity.getSalt());
        if(encryptedPassword.equals(customerEntity.getPassword())){
            JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(encryptedPassword);
            CustomerAuthEntity customerAuthEntity = new CustomerAuthEntity();
            customerAuthEntity.setCustomerEntity(customerEntity);
            customerAuthEntity.setUuid(customerEntity.getUuid());
            ZonedDateTime now = ZonedDateTime.now();
            ZonedDateTime expiresAt = now.plusHours(8);
            customerAuthEntity.setAccessToken(jwtTokenProvider.generateToken(customerEntity.getUuid(),now,expiresAt));
            customerAuthEntity.setLoginAt(now);
            customerAuthEntity.setExpiresAt(expiresAt);

            return customerDao.storeToken(customerAuthEntity);

        }else{
            throw new AuthenticationFailedException("ATH-002","Invalid Credentials");
        }

    }

    @Transactional(propagation = Propagation.REQUIRED)
    public CustomerEntity logout(String authorization) throws AuthorizationFailedException {

        String[] bearerToken = authorization.split("Bearer ");
        CustomerAuthEntity customerAuthEntity = customerDao.getCusotmerAuth(bearerToken[1]);
        if(customerAuthEntity==null){
            throw new AuthorizationFailedException("ATHR-001","Customer is not Logged in.");
        }
        if(customerAuthEntity.getLogoutAt()!=null){
            throw new AuthorizationFailedException("ATHR-002","Customer is logged out. Log in again to access this endpoint.");
        }
        ZonedDateTime now = ZonedDateTime.now();
        if(now.isAfter(customerAuthEntity.getExpiresAt())){
            throw new AuthorizationFailedException("ATHR-003","Your session is expired. Log in again to access this endpoint.");
        }

        customerAuthEntity.setLogoutAt(now);
        customerDao.updateCustomerAuthEntity(customerAuthEntity);
        return customerAuthEntity.getCustomerEntity();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public CustomerEntity updateCustomerEntity(String authorization,CustomerEntity customerEntity) throws UpdateCustomerException, AuthorizationFailedException {
        if(customerEntity.getFirstName()==null || customerEntity.getFirstName()==""){
            throw new UpdateCustomerException("UCR-002","First name field should not be empty");
        }
        String[] bearerToken = authorization.split("Bearer ");
        CustomerAuthEntity customerAuthEntity = customerDao.getCusotmerAuth(bearerToken[1]);
        if(customerAuthEntity==null){
            throw new AuthorizationFailedException("ATHR-001","Customer is not Logged in.");
        }
        if(customerAuthEntity.getLogoutAt()!=null){
            throw new AuthorizationFailedException("ATHR-002","Customer is logged out. Log in again to access this endpoint.");
        }
        ZonedDateTime now = ZonedDateTime.now();
        if(now.isAfter(customerAuthEntity.getExpiresAt())){
            throw new AuthorizationFailedException("ATHR-003","Your session is expired. Log in again to access this endpoint.");
        }

        CustomerEntity updatedCustomerEntity = customerAuthEntity.getCustomerEntity();
        updatedCustomerEntity.setFirstName(customerEntity.getFirstName());
        updatedCustomerEntity.setLastName(customerEntity.getLastName());
        customerDao.updateCustomerEntity(updatedCustomerEntity);
        return updatedCustomerEntity;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public CustomerEntity changePassword(String authorization,String oldPassword,String newPassword) throws AuthorizationFailedException, UpdateCustomerException {

        String[] bearerToken = authorization.split("Bearer ");
        CustomerAuthEntity customerAuthEntity = customerDao.getCusotmerAuth(bearerToken[1]);
        if(customerAuthEntity==null){
            throw new AuthorizationFailedException("ATHR-001","Customer is not Logged in.");
        }
        if(customerAuthEntity.getLogoutAt()!=null){
            throw new AuthorizationFailedException("ATHR-002","Customer is logged out. Log in again to access this endpoint.");
        }
        ZonedDateTime now = ZonedDateTime.now();
        if(now.isAfter(customerAuthEntity.getExpiresAt())){
            throw new AuthorizationFailedException("ATHR-003","Your session is expired. Log in again to access this endpoint.");
        }

        if(oldPassword == null || oldPassword == "" || newPassword ==null || newPassword == ""){
            throw new UpdateCustomerException("UCR-003","No field should be empty");
        }
        CustomerEntity customerEntity=customerAuthEntity.getCustomerEntity();
        String encryptedPassword = passwordCryptographyProvider.encrypt(oldPassword,customerEntity.getSalt());
        if(!encryptedPassword.equals(customerEntity.getPassword())){
            throw new UpdateCustomerException("UCR-004","Incorrect old password!");
        }

        Pattern upperCase = Pattern.compile("[A-Z]");
        Matcher upperCaseCheck = upperCase.matcher(newPassword);

        Pattern oneDigit = Pattern.compile("[0-9]");
        Matcher oneDigitCheck = oneDigit.matcher(newPassword);

        Pattern specialCharacters = Pattern.compile("[#@$%&*!^]");
        Matcher specialCharactersCheck = specialCharacters.matcher(newPassword);

        if(customerEntity.getPassword().length()<8 || !upperCaseCheck.find() || !oneDigitCheck.find() || !specialCharactersCheck.find() ){
            throw new UpdateCustomerException("UCR-001","Weak password!");
        }

        String[] encryptedText= passwordCryptographyProvider.encrypt(newPassword);
        customerEntity.setSalt(encryptedText[0]);
        customerEntity.setPassword(encryptedText[1]);

        customerDao.updateCustomerEntity(customerEntity);

        return customerEntity;

    }

}
