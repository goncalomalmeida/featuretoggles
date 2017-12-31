package com.experiments.toggles.persistence.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

/**
 * This is a generic interface for all the other repositories to implement and can be used the perform bulk
 * operations on all of them.
 * <p>
 * {@link NoRepositoryBean} means that this interface isn't an actual spring data repository, thus doesn't need to be
 * instantiated.
 *
 * @param <T> JPA managed entity
 * @param <I> id type of the JPA managed entity
 */
@NoRepositoryBean
public interface GenericRepository<T, I extends Serializable> extends CrudRepository<T, I> {

}
