package ru.mirea.ItemService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServiceForItem {
    private ItemDbConnection itConnect;

    @Autowired
    public ServiceForItem(ItemDbConnection itConnect){
        this.itConnect = itConnect;
    }
    public List<Item> getAll(){return itConnect.getAll();}
    public Item getItems (int id){return itConnect.getItems(id); }
    public List<Item> getPets_orStuffs(String pet){return itConnect.getPets_orStuffs(pet);}
    public Item getPet_orStuff (String pet, int id){return itConnect.getPet_orStuff(pet,id);}
    public String updateCountForItem(int id){return itConnect.updateCountForItem(id);}

    public Integer get_countOfItem(int id) {
        return itConnect.get_count(id);
    }
}