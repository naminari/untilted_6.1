package collection;

import builders.HumanDirector;
import exceptions.ValidException;
import humans.HumanBeing;
import humans.Mood;
import humans.WeaponType;
import io.XMLFileWriter;
import lombok.extern.slf4j.Slf4j;
import utils.FileChecker;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

@Slf4j
public class HumanSet implements HumanCollection<Queue<HumanBeing>>{
    private final Set<HumanBeing> collection;
    private final LocalDateTime creationDate;
    private final Validator<HumanBeing> validator;
    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    public String getDatabaseInfo() {
        return databaseInfo;
    }

    private final String databaseInfo;

    private final Lock readLock = readWriteLock.readLock();
    private final Lock writeLock = readWriteLock.writeLock();

    public HumanSet(Collection<HumanBeing> collection,String clientInfo, Validator<HumanBeing> validator) {
        this.databaseInfo = clientInfo;
        this.collection = new LinkedHashSet<>();
        this.validator = validator;
        collection.forEach(this::add);
        this.creationDate = LocalDateTime.now();
        log.info("Collection was initialized");
    }

    public Set<HumanBeing> getCollection() {
        return collection;
    }
    public int size() {
        readLock.lock();
        int size = collection.size();
        readLock.unlock();
        return size;
    }
    public String add(HumanBeing element) {
        try {
            String message;
            if (validator.checkElement(element) && !checkElementById(element.getId())) {
                writeLock.lock();
                collection.add(element);
                writeLock.unlock();
                message = String.format("HumanBeing with id: %s was added to collection", element.getId().toString());
            } else {
                message = String.format("element: %s is already exists", element.getId().toString());
            }
            log.info(message);
            return message;
        } catch (ValidException e) {
            log.error(e.getMessage());
            return e.getMessage();
        }
    }

    public Boolean Min(HumanBeing humanBeing) {
        if (collection.size() == 0) {
            return true;
        }
        if (getMinElement().get().compareTo(humanBeing) > 0) {
            return true;
        } else {
            return false;
//            return "Human more than min of collection or equals it";
        }
    }

    public boolean checkElementById(Integer id) {
        readLock.lock();
        boolean isExist = collection.stream()
                .map(HumanBeing::getId)
                .anyMatch(i -> i.equals(id));
        readLock.unlock();
        return isExist;
    }

    public Optional<HumanBeing> getMinElement() {
        return collection.stream().min(Comparator.comparingLong(HumanBeing::getImpactSpeed));
    }

    public boolean removeById(Integer id) {
        writeLock.lock();
        readLock.lock();
        boolean isRemoved = collection.removeIf(humanBeing -> humanBeing.getId().equals(id));
        writeLock.unlock();
        readLock.unlock();
        return isRemoved;
    }


    public String toString() {
        StringBuilder str = new StringBuilder();
        for (MainCollectible human : collection) {
            str.append(human.toString()).append("\n");
        }
        return str.toString();
    }

    public void removeLower(HumanBeing humanBeing) {
        collection.removeIf(humanBeing1 -> humanBeing1.compareTo(humanBeing) < 0);
    }

    public String countLessWeapon(WeaponType weaponType) {
        writeLock.lock();
        readLock.lock();
        String response = String.format("Count of elements less than %s - %d \n", weaponType.toString(), collection.stream()
                .map(HumanBeing::getWeaponType)
                .filter(weapon -> weapon.compareTo(weaponType) < 0).count());
        writeLock.unlock();
        readLock.unlock();
        return response;
    }

    public String filterByImpactSpeed(String arg) {
        int speed = Integer.parseInt(arg);
        StringBuilder str = new StringBuilder();
        for (MainCollectible human : collection) {
            if (speed == human.getImpactSpeed()) {
                str.append(human).append("\n");
            }
        }
        return str.toString();
    }

    public String filterGreaterThanMood(Mood mood) {
        StringBuilder str = new StringBuilder();
        for (MainCollectible human : collection) {
            Mood collectionMood = human.getMood();
            if (collectionMood != null && collectionMood.getNumber() - mood.getNumber() > 0) {
                str.append(human).append("\n");
            }
        }
        return str.toString();
    }

    public String show() {
        writeLock.lock();
        readLock.lock();
        String response = null;
        if (size() == 0) {
            response = "Collection is empty";
        } else {
            if (false) {
                response = collection.stream()
                        .limit(100)
                        .map(HumanBeing::toString)
                        .collect(Collectors.joining(System.lineSeparator())) + String.format("\n And %d products more ....", collection.size() - 100);
            } else {
                response = collection.stream()
                        .map(HumanBeing::toString)
                        .collect(Collectors.joining(System.lineSeparator()));
            }
        }
        writeLock.unlock();
        readLock.unlock();
        return response;
    }

}