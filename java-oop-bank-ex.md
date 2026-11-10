This project will work on creating a basic Bank console application. It will contain several classes to manage the operation of the bank, and some logic in main to allow user interation with the bank program.

The bank has several accounts, each account has some information about the person who the account belongs to and info about the account itself. Each account will allow users to deposit or withdraw money to/from it. There will be several speciality accounts which behave a bit differently when withdrawing or depositing money.

### Phase 1

In this phase we will implement the basic info and logic for the bank application. See the UML sketch:

<img width="1871" height="591" alt="image" src="https://github.com/user-attachments/assets/b64d1eaf-49e1-4b06-9fe1-e5d4201ff32a" />

`Address` and `Person` are helper classes representing info about the user of the account.

The `Account` class contains information about the bank account, and has methods `withdraw` and `deposit` which might be modified by extending classes. Both methods will return a `boolean`, indicating whether or not the operation was successful. If it was successful, `balance` will be modified to the new amount, otherwise `balance` should remain the same. Users should not be allowed to withdraw more than the current `balance`. There is no limit on deposit.

`OverdraftAccount` is an `Account` subclass which allows to withdraw more the the existing balance up to a given overdraft amount. So the minimum value of balance is `-overdraft`. Meaning that when `withdraw` is called, it should allow withdrawing up to a limit of `balance - overdraft`. If the `balance=5` and `overdraft=20`, users may withdraw up to a total of `25`.

`LimitedActionAccount` is an `Account` subclass which limits the amount in withdraw and deposit. If amount is bigger then limit, the method will fail and return false. So for `limit=5` when `withdraw` is called, if it is called with `amount>5` the method should return `false`.

`Bank` contains a list of all accounts in the bank and allows accessing and adding accounts.

### Phase 2

Implement the main part of the project.
This part will communicate with the user using input and output to perform operations with the bank. Use the classes from Phase 1 to perform the operation.

When starting the program, a menu will be open which will display all the options for actions to use. It will await an input of a number describing the option to perform. After performing the option, the program will return to the menu.

```
1. Create Account
2. Show Account
3. Deposit
4. Withdraw
5. exit
```

1. Enters Create Account menu. Here the program will request information necessary to construct an Account object. Once finished, this account will be stored in Bank. There are several types of Account, so the menu should also ask which type of Account to create. For each type of account, allow entering additional information used for the specific account: overdraft for `Overdraft` account, limit for `LimitedActionAccount`

```
Enter first name:
Abu
Enter last name: 
Dabi
Enter street: 
ul. Vosdvizhenka d.1
Enter city:
Moscow
Enter account type:
overdraft
Enter overdraft:
10
```

2. Enters Show Account menu.  Here the program will list all accounts (by name of the person), and allow the user to select with a number which account to show. Once select, the program will print all the account information, including type of account, balance, address and so on.

```
1. Your Mama
2. Barak Obama

2

First Name: Barak
Last Name: Obama
Street address: somewhere blvd. 12
City: Some city
Balance: 10423
Account Type: Normal
```

3. Enters Deposit menu. Here the program will list all accounts (like in 2). Selecting an account will allow depositing money into the account, and display whether or not it was successful.

```
1. Harley Quinn
2. Oarak Bbama

1

Enter amount:
20

Success
New Balance: 4320
```

4. Enters Withdraw menu. Here the program will list all accounts (like in 2). Selecting an account will allow withdrawing money from the account, and display whether or not it was successful.

```
1. Joseph Stalin
2. Bibi Habibi

1

Enter amount:
20

Success
New Balance: 4300
```

5. Exits the program.
