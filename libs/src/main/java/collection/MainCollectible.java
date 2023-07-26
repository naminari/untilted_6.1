package collection;

import humans.Mood;
import humans.WeaponType;

public interface MainCollectible {
    Long getImpactSpeed();
    Mood getMood();
    WeaponType getWeaponType();
}
