import java.util.*;
class BankingApp{
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        System.out.println("Welcome to Banking application");
        String[] customerDetails = {"11,11011,Kumar,10000,ApipNbjm","22,22022,Madhu,20000,Cboljoh","33,33033,Rahul,30000,dbnqvt","44,44044,Robin,40000,kbwb22"};
        List <Customer> cList = new ArrayList<>();
        List <Transaction> tList = new ArrayList<>();
        for(String c : customerDetails){
            String[] customer = c.split(",");
            String custId = customer[0];
            String accountNo = customer[1];
            String name = customer[2];
            Double balance = Double.parseDouble(customer[3]);
            String password = customer[4];
            Customer cus = new Customer(custId,accountNo,name,balance,password);
            cList.add(cus);
        }
        // for(Customer c: cList)
        //     System.out.println (c.accountNo);
        while(true){
            System.out.println("1.Existing User\n2.New User\n3.Admin");
            int type = sc.nextInt();
            sc.nextLine();
            switch(type){
                case 1:
                    Customer.existingCustomer(sc,cList,tList);
                    break;
                case 2:
                    Customer.createCustomer(sc,cList,tList);
                case 3:
                    Customer.adminDashboard(cList,sc);
            }
        }
    }
}

class Customer{
    String custId;
    String accountNo;
    String name;
    Double balance = 10000.0;
    String password;
    Integer transactionCount = 0;
    Queue<String> pastPasswords = new LinkedList<>(); 
    List<Transaction> transactions = new ArrayList<>();

    public Customer(String custId,String accountNo,String name,Double balance,String password){
        this.custId = custId;
        this.accountNo = accountNo;
        this.name = name;
        this.balance = balance;
        this.password = password;
    }

    public Customer(String custId,String accountNo,String name,String password){
        this.custId = custId;
        this.accountNo = accountNo;
        this.name = name;
        this.password = password;
    }

    public static void adminDashboard(List<Customer> cList,Scanner sc){
        System.out.println("Enter the number of customer");
        int n = sc.nextInt();
        System.out.println("CustId"+"  "+"AccountNo"+"  "+"Name"+"  "+"Balance"+"  "+"EncryptedPwd");
        for(int i = 0;i<n;i++){
            Customer c = cList.get(i);
            System.out.println(c.custId+"  "+c.accountNo+"  "+c.name+"  "+c.balance+"  "+c.password);
        }
        
    }

    public static void existingCustomer(Scanner sc, List<Customer> cList,List<Transaction> tList){
        System.out.println("Login with your Credential");
        Boolean flag = false;
        while(!flag){
            System.out.println("Customer Id:");
            String accountNo = sc.nextLine();
            System.out.println("Password:");
            String password = sc.nextLine();
            Customer c = cList.stream().filter(it->it.accountNo.equals(accountNo)).findFirst().orElse(null);
            if(c == null){
                System.out.println("Please enter valid accountNo");
            }else{
                if(password.equals(decryptPassword(c.password))){
                    System.out.println("Loged in Successfully");
                    flag = operations(c,cList,sc,tList);

                }else{
                    System.out.println("Please enter valid password");
                }

            }
        }
    }

    public static void createCustomer(Scanner sc, List<Customer> cList,List<Transaction> tList){
        System.out.println("Welcome!!\nEnter the name");
        String name = sc.nextLine();
        System.out.println("Password should contain least 2 lower case, 2 upper case with a minimum length of 6");
        System.out.println("Enter the password");
        String password = sc.nextLine();
        System.out.println("Enter the reEnter password");
        String reEnterPassword = sc.nextLine();
        if(password.equals(reEnterPassword)){
            Boolean isCorrect = passwordComplexity(password);
            if(isCorrect){
                int cId = Integer.parseInt(cList.get(cList.size()-1).custId)+1;            
                String custId = String.valueOf(cId);
                String accountNo = custId+"0"+custId;          
                String enCryptPass = encryptPassword(password);
                System.out.println("custId " + custId);
                Customer c = new Customer(custId,accountNo,name,enCryptPass);
                cList.add(c);
                Transaction t = new Transaction(tList.size() + 1,10000.0,"Opening",c.balance);
                System.out.print(t.balance+" "+t.amount+" "+t.transType+" "+t.transId);
                tList.add(t);
                c.transactions.add(t);
                System.out.println("Account created Successfully\nKindle Note down your account number for your reference\nAccount No:"+accountNo);
                existingCustomer(sc,cList,tList);
            }else{
                System.out.println("Please enter the password accordingly");
            }
        }else{
            System.out.println("Password and reEnterPassword is inCorrect, Try again:)");
        }
    }

    public static Boolean passwordComplexity(String password){
        
        return true;
    }

    public static String encryptPassword(String s){
        String encrypted = "";
        for(int i = 0;i<s.length();i++){
            char c = s.charAt(i);
            if(c == 'z' || c == 'Z'){
                encrypted += (char)(c-32);
            }else if(c == '9'){
                encrypted += (char)(c-9);
            }else{
                encrypted += (char)(c+1);
            }
        }
        return encrypted;
    }

    public static String decryptPassword(String s){
        String decrypted = "";
        for(int i = 0;i<s.length();i++){
            char c = s.charAt(i);
            if(c == 'a' || c == 'A'){
                decrypted += (char)(c+25);
            }else if(c == '0'){
                decrypted += (char)(c + 9);
            }else{
                decrypted += (char)(c-1);
            }
        }
        System.out.println(decrypted);
        return decrypted;
    }

    public static Boolean operations(Customer c,List<Customer>cList,Scanner sc,List<Transaction> tList){
        Boolean flag = false;
        while(true){            
            System.out.println("Operations:\na.ATM Withdrawal\nb.Cash Deposit\nc.Account Transfer\nd.View Transaction\ne.Home\nf.Exit");
            char type = sc.next().charAt(0);
            System.out.println("-------------------------"+ type);
            switch (type) {
                case 'a':
                    cashWithDrawal(c,sc,tList);
                    break;
                case 'b':
                    cashDeposit(c,sc,tList);
                    break;
                case 'c':
                    AccountTransfer(c,sc,tList,cList);
                    break;
                case 'd':
                    Transaction.transactionHistory(c);
                    break;
                case 'e':
                    flag = true;
                    break;
                case 'f':
                    sc.nextLine();
                    existingCustomer(sc, cList,tList);
                    break;
                default:
                    System.out.println("Enter a valid option");
                    break;
            }
            if(flag)
                break;
        }
        return flag;
    }

    public static void cashWithDrawal(Customer c,Scanner sc,List<Transaction> tList){
        System.out.println("Cash Withdrawal\nEnter the amount to withdraw");
        sc.nextLine();        
        Double amount = Double.parseDouble(sc.nextLine());
        if(c.balance >= amount){
            if((c.balance - 1000) > amount){
                c.balance -= amount;
                Transaction t = new Transaction(tList.size() + 1,amount,"ATMWithdrawal",c.balance);
                tList.add(t);
                c.transactions.add(t);
                c.transactionCount++;
                if(amount > 5000){
                    c.balance -= 10.0;
                    t = new Transaction(tList.size() + 1,10.0,"Nominal Fees",c.balance);
                    c.transactions.add(t);
                }
                System.out.println("Amount Withdrawed");
                System.out.println("Balance: "+c.balance);
            }else{
                System.out.println("Your account has to maintain a Minimum Balance of 1000, Your current balance is "+c.balance);    
            }
        }else{
            System.out.println("You have insufficient Balance: "+c.balance);
        }
    }

    public static void cashDeposit(Customer c,Scanner sc,List<Transaction> tList){
        System.out.println("Cash Deposit\nEnter the amount to Deposit");
        sc.nextLine();
        Double amount = Double.parseDouble(sc.nextLine());
        c.balance += amount;
        Transaction t = new Transaction(tList.size() + 1,amount,"CashDeposit",c.balance);
        tList.add(t);
        c.transactions.add(t);
        c.transactionCount++;
        System.out.println("Amount Deposited");
        System.out.println("Balance: "+c.balance);
    }

    public static void AccountTransfer(Customer c,Scanner sc,List<Transaction> tList,List<Customer> cList){        
        System.out.println("Account Transfer");
        sc.nextLine();
        System.out.println("Enter the Beneficiary AccountNo:");
        String to = sc.nextLine();
        Customer toCustomer = cList.stream().filter(it->it.accountNo.equals(to)).findFirst().orElse(null);
        if(toCustomer == null){
            System.out.println("Enter a valid accountNo ");
            return;
        }
        System.out.println("Enter the amount to transfer");
        Double amount = sc.nextDouble();
        if(c.balance >= amount){
            if((c.balance - 1000) > amount){
                c.balance -= amount;
                Transaction t = new Transaction(tList.size() + 1,amount,"TransferTo "+toCustomer.accountNo,c.balance);
                tList.add(t);
                c.transactions.add(t);
                c.transactionCount++;
                if(amount > 5000){
                    c.balance -= 10.0;
                    t = new Transaction(tList.size() + 1,10.0,"Nominal Fees",c.balance);
                    c.transactions.add(t);
                }
                toCustomer.balance += amount;
                t = new Transaction(tList.size() + 1,amount,"TransferFrom "+c.accountNo,toCustomer.balance);
                tList.add(t);
                toCustomer.transactions.add(t);
                System.out.println("Amount Transferred to AccountNo: "+toCustomer.accountNo);
                System.out.println("Balance: "+c.balance);
            }else{
                System.out.println("Your account has to maintain a Minimum Balance of 1000, Your current balance is "+c.balance);    
            }
        }else{
            System.out.println("You have insufficient Balance: "+c.balance);
        }
    }
}

class Transaction{
    Integer transId;
    Customer from;
    Customer to;
    Double amount;
    String transType;
    Double balance;

    public Transaction(int transId,Double amount, String transType,Double balance){
        this.transId = transId;
        this.amount = amount;
        this.transType = transType;
        this.balance = balance;
    }

    public static void transactionHistory(Customer c){
        if(c.transactions.size() > 0){
            System.out.println("/bAccount Statement\b");
            System.out.println("Name - "+c.name);
            System.out.println("Account No - "+c.accountNo);
            System.out.println("Customer Id - "+c.custId);
            System.out.println("TransID  TransType  Amount  Balance");
            for(Transaction t: c.transactions){
                System.out.println(t.transId+"  "+t.transType+"  "+t.amount+"  "+t.balance);
            }
        }else{
            System.out.println("No transactions so far!!");
        }
    }
}