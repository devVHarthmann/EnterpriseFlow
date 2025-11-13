/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ulbra.model;

/**
 *
 * @author aluno.saolucas
 */
public class Usuario {

    private int id_usuario;
    private String nome;
    private String email;
    private String cargo;
    private String senha;

    public Usuario() {

    }

    public Usuario(int id_usuario, String nome, String email, String cargo) {
        this.id_usuario = id_usuario;
        this.nome = nome;
        this.email = email;
        this.cargo = cargo;
    }

    public Usuario(int id_usuario, String nome, String email, String cargo, String senha) {
        this.id_usuario = id_usuario;
        this.nome = nome;
        this.email = email;
        this.cargo = cargo;
        this.senha = senha;
    }

    public Usuario(int id_usuario, String nome) {
        this.id_usuario = id_usuario;
        this.nome = nome;
    }
    
    
    
    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    @Override
    public String toString() {
        return nome; // Importante: assim o JComboBox vai mostrar o nome
    }

}
