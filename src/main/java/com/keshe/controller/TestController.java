package com.keshe.controller;

import com.keshe.entity.RestBean;
import com.keshe.entity.SysUser;
import com.keshe.entity.Test;
import com.keshe.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/test")
@RestController
public class TestController {
    @Autowired
    UserService userService;

    @GetMapping("test{id}")
    public RestBean test(int id ,String name) {
        return RestBean.success("test" + id+"name"+name);
    }

    @GetMapping("test/{id}")
    public RestBean test2(@PathVariable int id) {
        return RestBean.success("test" + id);
    }

   @PostMapping("test3")
public RestBean test3(@RequestBody Test test) {
    Test test1 = new Test();
    test1.setId(test.getId());
    test1.setName(test.getName());
    test1.setAge(test.getAge());
    return RestBean.success(test1);
}
@GetMapping("test4")
    public RestBean test4(@RequestBody int id,@RequestBody String name){

        return RestBean.success("id"+id+"name"+name);
}

@GetMapping("test5")
    public RestBean test5(){
    System.out.println(userService.AllUser());
    return RestBean.success(userService.AllUser());}

}
