package com.hypermarket.user;

//الكلاس ده هو اللي هيعرف شكل المستخدمين اللي شغالين عندك في السيستم
// (زي موديل أو temp لكل يوزر)
public class User {
    private int id;
    private String username;
    private String password;
    private String role;

    //كل يوزر عنده شوية بيانات:
    //
    //id: رقم مميز لكل مستخدم (Unique ID).
    //
    //username: اسم المستخدم اللي هيدخل بيه.
    //
    //password: الباسورد بتاعه.
    //
    //role: دوره في السيستم (مثلاً "admin" أو "user").
    //
    //وكلمة private معناها إن الحاجات دي متقفولة ومحدش يقدر يوصلها مباشرة من برة الكلاس.

    public User(int id, String username, String password, String role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
    }
    //this	معناها "المتغير اللي جوه  مش اللي جايلي من البرة"

    //Constructor
    // ده بيتنفذ أول ما تعمل يوزر جديد.
    //
    //بياخد 4 معلومات (id, username, password, role) ويخزنهم جوه الكلاس.
    //
    //كلمة this معناها "المتغير اللي جوه الكلاس نفسه".

    // Getters and Setters

    //الـ Getter هو method بترجعلك قيمة المتغير (بتاخدها بس، مش بتغيرها).
    //
    //الـ Setter هو method بتغير قيمة المتغير (بتسمحلك تعدلها من بره).

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    @Override
    public String toString() {
        return id + "," + username + "," + password + "," + role;
    }

    //هنا بتغير طريقة عرض اليوزر لما تيجي تطبعه.

}
