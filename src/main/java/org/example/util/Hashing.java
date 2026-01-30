package org.example.util;

import org.mindrot.jbcrypt.BCrypt;

public class Hashing {
        private static final int SALT_VALUE=5;
        public static String hashPassword(String pass){

            return BCrypt.hashpw(pass,BCrypt.gensalt(SALT_VALUE));
        }

        public static boolean verifyPassword(String password, String hashPassword) {
            return BCrypt.checkpw(password,hashPassword);
        }

        private Hashing(){}
    }

