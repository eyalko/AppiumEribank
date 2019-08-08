package Settings;


public enum CloudUsers {

    Example("name","pass", "default", false,"111.111.1.111", 80),
    EyalDeepCloudAdmin("eyalk", "Jj123456" ,"eyJ4cC51IjoxNTg0ODA0LCJ4cC5wIjoyLCJ4cC5tIjoiTVRVMU5ERXdORFF6TmprMU13IiwiYWxnIjoiSFMyNTYifQ.eyJleHAiOjE4Njk0NjQ0MzYsImlzcyI6ImNvbS5leHBlcml0ZXN0In0.cuilvVOQwSXcXr2uV9pOzuw3yqhjtSrdUsq-Ilj57xQ" ,"Default" , true , "qa-win2016.experitest.com" ,443),
    EyalQA("eyalko", "Experitest2012" ,"eyJ4cC51IjoyMDc3NjkyLCJ4cC5wIjoyLCJ4cC5tIjoiTVRVMU1USTBOVFF6TVRRMU9BIiwiYWxnIjoiSFMyNTYifQ.eyJleHAiOjQ3MDQ4NDU0MzEsImlzcyI6ImNvbS5leHBlcml0ZXN0In0.4mUK21-Veqb6I091Hyruc-OSoZNiqPy_i9Cz--AFg3c" ,"Default" , true , "qacloud.experitest.com" ,443),
    LocalCloud("admin", "Experitest2012" ,"eyJ4cC51Ijo0MCwieHAucCI6MiwieHAubSI6Ik1UVTFNVEkwTnpFME5qYzNNUSIsImFsZyI6IkhTMjU2In0.eyJleHAiOjE4NjY2MDcxNDYsImlzcyI6ImNvbS5leHBlcml0ZXN0In0.4prLo143APZywI57UhOMrqITTaWP1NUKC280QtpC1mU" ,"Default" , false , "ekopelevich-lt" ,9090)

    ;

    public String userName;
    public String Password;
    public String projectName;
    public boolean isSecured;
    public String grid_domain; //without https
    public int grid_port;
    //public String Authorization;
    public String AccessKey;


    CloudUsers(String userName, String Password , String projectName, Boolean isSecured ,
               String grid_domain , int grid_port){
        this.userName=userName;
        this.Password=Password;
        this.projectName=projectName;
        this.isSecured=isSecured;
        this.grid_domain=grid_domain;
        this.grid_port=grid_port;
        this.AccessKey="0";
    }

    CloudUsers(String userName, String Password ,String AccessKey ,String projectName , Boolean isSecured ,
               String grid_domain , int grid_port){
        this.userName=userName;
        this.Password=Password;
        this.AccessKey=AccessKey;
        this.projectName=projectName;
        this.isSecured=isSecured;
        this.grid_domain=grid_domain;
        this.grid_port=grid_port;

    }

    CloudUsers(String AccessKey , String projectName , Boolean isSecured ,
               String grid_domain , int grid_port){
        this.AccessKey=AccessKey;
        this.projectName=projectName;
        this.isSecured=isSecured;
        this.grid_domain=grid_domain;
        this.grid_port=grid_port;
    }

//    public String toString(boolean PrintInOneLine){
//
//        if(PrintInOneLine){
//            String starts="http";
//            if(isSecured) starts+="s";
//            return starts+"://"+grid_domain+":"+grid_port+"   |    "+"UserName: "+userName;
//        }else
//            return "## Cloud User: ##"+ Main.delimiter+
//                    "userName: "+userName+ Main.delimiter+
//                    "projectName: "+projectName+ Main.delimiter+
//                    "isSecured: "+isSecured+ Main.delimiter +
//                    "grid_domain: "+grid_domain+ Main.delimiter +
//                    "grid_port: "+grid_port + Main.delimiter
//                    ;
//    }


//    public String toString(){
//        return "## Cloud User: ##"+ Main.delimiter+
//                "userName: "+userName+ Main.delimiter+
//                "projectName: "+projectName+ Main.delimiter+
//                "isSecured: "+isSecured+ Main.delimiter +
//                "grid_domain: "+grid_domain+ Main.delimiter +
//                "grid_port: "+grid_port + Main.delimiter
//                ;
//    }

    public String getUserName(){return userName;}
    public String getPassword(){return Password;}
    public String getAccessKey(){return AccessKey;}
    public String getprojectName(){return projectName;}
    public String getCloudFullAddress(){
        String starts="http";
        if(isSecured) starts+="s";
        return starts+"://"+grid_domain+":"+grid_port;
    }

}
