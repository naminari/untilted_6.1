package builders;

import collection.Validator;
import exceptions.ValidException;
import humans.HumanBeing;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class HumanDirector {
    private final List<Method> humanBeingMethods;
    private final Validator<HumanBeing> humanBeingValidator;

    {
        humanBeingMethods = getMethodsByOrder(ArgsHumanBuilder.class);
    }
    public HumanDirector(Validator<HumanBeing> validator){
        this.humanBeingValidator = validator;
    }
    public HumanBeing buildHuman(String ... args) throws ValidException, InvocationTargetException, IllegalAccessException {
        int sumArgsOfMethods = countValueOfParameters(humanBeingMethods);
        if (sumArgsOfMethods == args.length) {
            return buildHumanWithArgs(humanBeingMethods, args);
        } else {
            HumanConsoleBuilder builder = new HumanConsoleBuilder(new HumanBeing()) {{
                setId();
                setName();
                setCoordinates();
                setCreationDate();
                setImpactSpeed();
                setMood();
                setRealHero();
                setWeaponType();
                setToothPick();
                setCar();

            }};
            return builder.getHuman();
        }
    }

    private <T> List<Method> getMethodsByOrder(Class<T> clazz) {
        return Arrays.stream(clazz.getDeclaredMethods())
                .filter(method -> !Objects.isNull(method.getAnnotation(Order.class)))
                .sorted(Comparator.comparingInt(method -> method.getAnnotation(Order.class).value()))
                .collect(Collectors.toList());
    }

    private HumanBeing buildHumanWithArgs(List<Method> humanMethods, String[] args) throws ValidException, InvocationTargetException, IllegalAccessException {
        int count = 0;
        ArgsHumanBuilder argsHumanBuilder = new ArgsHumanBuilder();
        for (Method method : humanMethods) {
            if (method.getParameterCount() == 1) {
                method.invoke(argsHumanBuilder, args[count]);
                count++;
            } else if (method.getParameterCount() == 2) {
                method.invoke(argsHumanBuilder, args[count], args[count + 1]);
                count = count + 2;
            }
        }
        argsHumanBuilder.setId();
        return argsHumanBuilder.getHuman(humanBeingValidator);
    }

    private int countValueOfParameters(List<Method> list) {
        int count = 0;
        for (Method method : list) {
            count = count + method.getParameterCount();
        }
        return count;
    }
}

