package collection;

import exceptions.ValidException;


public interface Validator<T> {
    boolean checkElement(T object) throws ValidException, ValidException;
}
