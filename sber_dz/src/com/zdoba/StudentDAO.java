package com.zdoba;

public interface StudentDAO {
    Student findByLastName(String lastName);
    Student findById(long id);
}
