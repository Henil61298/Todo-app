package com.springboot.todowebapp.todo;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

@Service
public class ToDoService {

//    @Autowired
    private static List<Todo> todos = new ArrayList<>();

    private static int countTodo = 0;

    static {
        todos.add(new Todo(++countTodo, "Udemy", "Learn Spring",
                LocalDate.now().plusYears(1), false));
        todos.add(new Todo(++countTodo, "Youtube", "Learn Springboot",
                LocalDate.now().plusYears(2), false));
        todos.add(new Todo(++countTodo, "Microsoft", "Learn Azure",
                LocalDate.now().plusYears(3), false));
    }

    public List<Todo> findByUser(String username){
        Predicate<? super Todo> predicate = todo -> todo.getUsername().equalsIgnoreCase(username);
        return todos.stream().filter(predicate).toList();
    }

    public static void addTodo(String name, String Description, LocalDate date, boolean isDone){
        Todo newTodo = new Todo(++countTodo, name, Description, date, isDone);
        todos.add(newTodo);
    }

    public static void deleteById(int id){
        for (int i=0;i<todos.size();i++){
            if (todos.get(i).getId() == id){
                todos.remove(todos.get(i));
            }
        }
    }

    public Todo findById(int id) {
        for (Todo todo : todos) {
            if (todo.getId() == id) {
                return todo;
            }
        }
        return null;
    }

    public void updateTodo(@Valid Todo todo) {
        deleteById(todo.getId());
        todos.add(todo); 
    }
}
