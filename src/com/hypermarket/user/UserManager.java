package com.hypermarket.user;

import com.hypermarket.util.FileUtil;
import java.util.List;

public class UserManager {
    private List<User> users;

    //هنا عرفنا ليست هتحط فيها كل اليوزرز اللي موجودين عندنا.

    public UserManager() {
        users = FileUtil.readUsersFromFile();
    }

    //لما تعمل أوبجكت جديد من UserManager،
    // أوتوماتيك هيقرأ كل المستخدمين من الملف ويحطهم جوا الـ users.

    public User login(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }

    //بيدور جوا الليست users:
    //
    //لو لقى يوزر بنفس الاسم والباسورد --> يرجعه.
    //
    //لو ملقاش --> يرجع null.

    public boolean createUser(String username, String password, String role) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return false; // اسم مستخدم موجود قبل كدا
            }
        }

        int newId = users.size() > 0 ? users.get(users.size() - 1).getId() + 1 : 1;
        User newUser = new User(newId, username, password, role);
        users.add(newUser);
        FileUtil.writeUsersToFile(users);
        users = FileUtil.readUsersFromFile();
        return true;
    }

    //بيشوف لو في يوزر بنفس الـ username موجود أصلا --> بيرجع false ومش بيضيفه.
    //
    //لو مش موجود:
    //
    //بيعمل ID جديد (آخر ID + 1).
    //
    //بيضيف المستخدم الجديد لليست.
    //
    //بيحفظ list كلها تاني في file.



    public boolean updateUser(int id, String newUsername, String newPassword) {
        for (User user : users) {
            if (user.getId() == id) {
                if (newUsername != null && !newUsername.isEmpty()) {
                    user.setUsername(newUsername);
                }
                if (newPassword != null && !newPassword.isEmpty()) {
                    user.setPassword(newPassword);
                }
                // حفظ التحديثات
                FileUtil.writeUsersToFile(users);
                users = FileUtil.readUsersFromFile();
                return true;
            }
        }
        return false; // لم يتم العثور على مستخدم بالـ ID
    }

    //ده لو معاك ID المستخدم وعايز تحدث بياناته:
    //
    //بتغير الـ username أو الباسورد لو فيه حاجة جديدة.
    //
    //تحفظ list تاني.


    // إضافة ميزة تعديل البيانات بعد تسجيل الدخول
    public boolean updateOwnData(User loggedInUser, String newUsername, String newPassword) {
        if (loggedInUser == null) {
            return false; // إذا كان المستخدم غير مسجل دخول
        }

        // تحديث اسم المستخدم أو كلمة المرور
        if (newUsername != null && !newUsername.isEmpty()) {
            loggedInUser.setUsername(newUsername);
        }
        if (newPassword != null && !newPassword.isEmpty()) {
            loggedInUser.setPassword(newPassword);
        }

        // حفظ التحديثات
        FileUtil.writeUsersToFile(users);
        users = FileUtil.readUsersFromFile();
        return true;
    }
}


//دي فكرة قوية ومهمة جدا:
//
//لو واحد سجل دخول وعايز يعدل بياناته (من غير ما يكتب ID ولا حاجه).
//
//بتعمل الآتي:
//
//تتأكد إن المستخدم اللي دخل مش فاضي (loggedInUser != null).
//
//لو فيه اسم جديد أو باسورد جديد --> تحدثهم.
//
//تحفظ التحديثات عالفايل.
