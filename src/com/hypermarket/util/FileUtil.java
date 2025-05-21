
//الكلاس ده مسؤول عن التعامل مع الملف اللي بنسجل فيه معلومات اليوزرز (اسمه users.txt).
//
//يعني:
//
//يقرأ كل اليوزرز من الملف ويحطهم في ليست.
//
//يكتب اليوزرز اللي في الليست ويرجع يخزنهم في الملف.
//
//بمعنى أبسط:
//
//كأنه وسيط بين برنامج وبين الملف اللي عالهارد ديسك.


// مسار الملف: com/hypermarket/util/FileUtil.java
package com.hypermarket.util;

import com.hypermarket.user.User;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {
    private static final String FILE_PATH = "users.txt";

    //هنا عرفنا إن كل عمليات القراءة والكتابة هتتعامل مع ملف اسمه users.txt.
    //
    //ومكتوب جنب البرنامج (في نفس الفولدر)

    public static List<User> readUsersFromFile() {
        List<User> users = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    int id = Integer.parseInt(parts[0]);
                    String username = parts[1];
                    String password = parts[2];
                    String role = parts[3];
                    users.add(new User(id, username, password, role));
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading users file: " + e.getMessage());
        }

        return users;
    }

    //بنجهز ليست فاضية users.
    //
    //بنفتح الملف باستخدام BufferedReader.
    //
    //بنقرأ الملف سطر سطر (while ((line = reader.readLine()) != null)).
    //
    //كل سطر بنقطعه (split(",")) علشان نطلع:
    //
    //id
    //
    //username
    //
    //password
    //
    //role
    //
    //بنحولهم لكائن User جديد ونضيفه للليست.
    //
    //في الآخر بنرجع ليست فيها كل اليوزرز.


//يعني
    //الملف فيه معلومات مكتوبة كـ"كلام عادي".
    //
    //البرنامج يقرأ كل سطر.
    //
    //يحول السطر ده إلى "مستخدم حي" جوه البرنامج (كائن User).
    //
    //يجمع كل المستخدمين دول جوا ليست.
    //
    //وبعدين يبعتلك الليست دي تقولك:
    //
    //اتفضل، دول كل اليوزرز الجاهزين اللي كانوا محفوظين!

    public static void writeUsersToFile(List<User> users) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {


            //بدل ما تفتح وتقفل بإيدك، تقول للكمبيوتر:
            //
            //"أنا هفتح الحاجة دي، وإنت لما أخلص اقفلها لوحدك، مش عايز أشيل همها."

            for (User user : users) {
                writer.write(user.getId() + "," + user.getUsername() + "," + user.getPassword() + "," + user.getRole());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error writing users file: " + e.getMessage());
        }
    }

    public static List<String> readLines(String ordersFile) {
        return List.of();
    }

    public static void writeLines(String ordersFile, List<String> lines) {
    }

    //في البرمجة، كلمة resource يعني حاجة بتستهلك من الجهاز:
    //
    //زي ملف فتحته (زي FileReader أو BufferedReader).
    //
    //أو قاعدة بيانات.
    //
    //أو شبكة إنترنت.
    //
    //لما تفتح ملف، المفروض لما تخلص منه، تقفله بنفسك علشان ما تسيبش الحاجة دي مفتوحة على الجهاز.
    //لو سبتها مفتوحة = بتعمل مشكلة اسمها Resource Leak يعني الجهاز فيه حاجة مفتوحة ملهاش لازمة ومستهلكة الرام أو البروسيسور.
}

//try-with-resources = بتفتح ملف أو حاجة... وتخلي جافا تقفله لوحدها لما تخلص.
//ومش محتاج تكتب close() بإيدك خالص.
