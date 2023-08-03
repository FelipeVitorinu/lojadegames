package com.projeto.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.projeto.demo.model.Produtos;

@Repository
public interface ProdutoRepository extends JpaRepository <Produtos, Long> {

	public List<Produtos> findAllByNomeContainingIgnoreCase(@Param("nome") String nome);
}
