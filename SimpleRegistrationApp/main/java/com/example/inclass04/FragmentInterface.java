package com.example.inclass04;

//InCLass04
//FragmentInterface.java
//Student 1: Krithika Kasargod
//Student 2: Sahana Srinivas

public interface FragmentInterface {
    void sendAccount(DataServices.Account account);

    void sendAccountFromRegister(DataServices.Account account);

    void gotoRegisterFragment();

    void gotoLoginFragment();

    void gotoUpdateFragment(DataServices.Account account);

    void gotoAccountFragment();

    void gotoAccountFragmentWithAccount(DataServices.Account account);

    void logoutAccount();
}
