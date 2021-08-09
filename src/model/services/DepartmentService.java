package model.services;

import java.util.ArrayList;
import java.util.List;
import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.entities.Department;

public class DepartmentService {
    
    //cria dependencia com o Department do banco de dados
    private DepartmentDao dao = DaoFactory.createDepartmentDao();
    
    public List<Department> findAll(){      
        //MOCK DATA => retornar dados ficticios
//        List<Department> list = new ArrayList<>();
//        list.add(new Department(1, "Books"));
//        list.add(new Department(2, "Revistas"));
//        list.add(new Department(3, "E-Books"));
//        list.add(new Department(4, "Games"));
//        list.add(new Department(5, "Infantil"));
//        return list;
        //---FIM DO MOCK--------------------------
        
        return dao.findAll();
    }
    
    public void saveOrUpdate(Department obj){
        if(obj.getId() == null){
            dao.insert(obj);
        }else{
            dao.update(obj);
        }
    }
    
    public void remove(Department obj){
        dao.deleteById(obj.getId());
    }
}
