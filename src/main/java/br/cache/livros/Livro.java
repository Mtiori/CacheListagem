package br.cache.livros;

import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "livro")
public class Livro {

  public static final String CACHE_NAME = "livro";

  @Id
  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid", strategy = "uuid2")
  private String id;
  
  @Column(name = "titulo")
  private String titulo;
  
  @Column(name = "autor")
  private String autor;

  public Livro(String titulo, String autor) {
    this();
    this.titulo = titulo;
    this.autor = autor;
  }

  public Livro() {
    id = UUID.randomUUID().toString();
  }

  public String getId() {
    return id;
  }

  public String getTitulo() {
    return titulo;
  }

  public String getAutor() {
    return autor;
  }

  @Override
  public int hashCode() {
    final int prime = 30;
    int result = 1;
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Livro other = (Livro) obj;
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
      return false;
    return true;
  }
}