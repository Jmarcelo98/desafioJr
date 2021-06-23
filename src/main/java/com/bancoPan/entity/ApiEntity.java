package com.bancoPan.entity;

import java.io.Serializable;

public class ApiEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	private String nome;
	private Double salario;
	private Double valorPedido;
	private Double valorEmprestado;
	private Double quantidadeParcelas;
	private Double valorParcela;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Double getSalario() {
		return salario;
	}

	public void setSalario(Double salario) {
		this.salario = salario;
	}

	public Double getValorPedido() {
		return valorPedido;
	}

	public void setValorPedido(Double valorPedido) {
		this.valorPedido = valorPedido;
	}

	public Double getValorEmprestado() {
		return valorEmprestado;
	}

	public void setValorEmprestado(Double valorEmprestado) {
		this.valorEmprestado = valorEmprestado;
	}

	public Double getQuantidadeParcelas() {
		return quantidadeParcelas;
	}

	public void setQuantidadeParcelas(Double quantidadeParcelas) {
		this.quantidadeParcelas = quantidadeParcelas;
	}

	public Double getValorParcela() {
		return valorParcela;
	}

	public void setValorParcela(Double valorParcela) {
		this.valorParcela = valorParcela;
	}

}
