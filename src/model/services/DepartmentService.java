package model.services;

import java.util.ArrayList;
import java.util.List;
import model.entities.Department;

public class DepartmentService {
    
    public List<Department> findAll(){//MOCK = retornar dados ficticios
        List<Department> list = new ArrayList<>();
        
        list.add(new Department(1, "Books"));
        list.add(new Department(2, "Computer"));
        list.add(new Department(3, "Cellphone"));
        list.add(new Department(4, "Video Games"));
        
        return list;
    }
}
