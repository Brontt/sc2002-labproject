package util.io;

import java.util.List;

/**
 * Interface for entity loaders (Open/Closed Principle, Dependency Inversion Principle).
 * Allows different implementations for loading entities from various sources.
 * @param <T> The type of entity to load
 */
public interface EntityLoader<T> {
    
    /**
     * Loads entities from a data source.
     * @param filename Path to the data file
     * @return List of loaded entities
     */
    List<T> load(String filename);
    
    /**
     * Saves entities to a data source.
     * @param filename Path to the data file
     * @param entities List of entities to save
     */
    void save(String filename, List<T> entities);
}