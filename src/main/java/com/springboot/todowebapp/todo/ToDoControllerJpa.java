package com.springboot.todowebapp.todo;

import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
@SessionAttributes("name")
public class ToDoControllerJpa {

//    @Autowired
//    private ToDoService toDoService;

    private TodoRepository todoRepository;

    public ToDoControllerJpa(ToDoService toDoService, TodoRepository todoRepository) {
//        this.toDoService = toDoService;
        this.todoRepository = todoRepository;
    }

    @RequestMapping("todo-list")
    public String listAllTodos(ModelMap model){
        String username = getLoggedinUsername(model);
        List<Todo> todos = todoRepository.findByUsername(username);
        model.addAttribute("todos", todos);
        return "listTodos";
    }

    @RequestMapping(value = "/add-todo", method = RequestMethod.GET)
    public String addTodo(ModelMap model){
        String username = getLoggedinUsername(model);
        Todo todo = new Todo(0, username, "", LocalDate.now().plusYears(1), false);
        model.put("todo", todo);
        return "todo";
    }

    @RequestMapping(value = "/add-todo", method = RequestMethod.POST)
    public String list(ModelMap model, @Valid Todo todo, BindingResult binding){
        if (binding.hasErrors()){
            return "todo";
        }
        String username = getLoggedinUsername(model);
        todo.setUsername(username);
        todoRepository.save(todo);
//        ToDoService.addTodo(username, todo.getDescription(), todo.getTargetDate(), todo.isDone());
        return "redirect:todo-list";
    }

    @RequestMapping("delete-todo")
    public String deleteATodo(@RequestParam int id){
        todoRepository.deleteById(id);
//        ToDoService.deleteById(id);
        return "redirect:todo-list";
    }

    @RequestMapping(value = "update-todo", method = RequestMethod.GET)
    public String showUpdateById(@RequestParam int id, ModelMap model){
        Todo todo = todoRepository.findById(id).get();
        model.addAttribute("todo", todo);
        return "todo";
    }

    @RequestMapping(value = "update-todo", method = RequestMethod.POST)
    public String updateTodo(ModelMap model, @Valid Todo todo, BindingResult binding){
        if (binding.hasErrors()){
            return "todo";
        }
        String username = getLoggedinUsername(model);
        todo.setUsername(username);
        todoRepository.save(todo);
//        toDoService.updateTodo(todo);
        return "redirect:todo-list";
    }

    private String getLoggedinUsername(ModelMap model){
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        return authentication.getName();
    }
}
