package ru.mirea.ItemService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class ItemController {

    @Autowired
    private ServiceForItem itemS;

    @RequestMapping(value = "user/item", method = RequestMethod.GET)
    @ResponseBody
    public List<Item> items() {
        return itemS.getAll();
    }

    @RequestMapping(value = "user/item/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Item itemId(@PathVariable int id) {
        return itemS.getItems(id);
    }

    @RequestMapping(value = "user/{pets}", method = RequestMethod.GET)
    @ResponseBody
    public List<Item> pets(@PathVariable String pets) {
        return itemS.getPets_orStuffs(pets);
    }

    @RequestMapping(value = "user/{pet}/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Item petId(@PathVariable String pet, @PathVariable int id ){
        return itemS.getPet_orStuff(pet, id);
    }

    @RequestMapping(value = "user/item/count/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Integer get_countOfItem(@PathVariable int id ){
      return  itemS.get_countOfItem(id);
    }

    //Запретить пользователю в курсовой
    @RequestMapping(value = "admin/update/count/{id}", method = RequestMethod.POST)
    @ResponseBody
    public String updateCount(@PathVariable int id ){
        return itemS.updateCountForItem(id);
    }
}