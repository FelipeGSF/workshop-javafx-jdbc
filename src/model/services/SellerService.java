package model.services;

import java.util.ArrayList;
import java.util.List;
import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Seller;

public class SellerService {
    
    //cria dependencia com o Seller do banco de dados
    private SellerDao dao = DaoFactory.createSellerDao();
    
    public List<Seller> findAll(){      
        //MOCK DATA => retornar dados ficticios
//        List<Seller> list = new ArrayList<>();
//        list.add(new Seller(1, "Books"));
//        list.add(new Seller(2, "Revistas"));
//        list.add(new Seller(3, "E-Books"));
//        list.add(new Seller(4, "Games"));
//        list.add(new Seller(5, "Infantil"));
//        return list;
        //---FIM DO MOCK--------------------------
        
        return dao.findAll();
    }
    
    public void saveOrUpdate(Seller obj){
        if(obj.getId() == null){
            dao.insert(obj);
        }else{
            dao.update(obj);
        }
    }
    
    public void remove(Seller obj){
        dao.deleteById(obj.getId());
    }
}
