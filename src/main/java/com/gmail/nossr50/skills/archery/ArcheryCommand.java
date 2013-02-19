package com.gmail.nossr50.skills.archery;

import com.gmail.nossr50.locale.LocaleLoader;
import com.gmail.nossr50.skills.SkillCommand;
import com.gmail.nossr50.skills.utilities.SkillType;
import com.gmail.nossr50.util.Permissions;

public class ArcheryCommand extends SkillCommand {
    private String skillShotBonus;
    private String dazeChance;
    private String dazeChanceLucky;
    private String retrieveChance;
    private String retrieveChanceLucky;

    private boolean canSkillShot;
    private boolean canDaze;
    private boolean canRetrieve;

    public ArcheryCommand() {
        super(SkillType.ARCHERY);
    }

    @Override
    protected void dataCalculations() {
        //SKILL SHOT
        double bonus = (skillValue / Archery.skillShotIncreaseLevel) * Archery.skillShotIncreasePercentage;

        if (bonus > Archery.skillShotMaxBonusPercentage) {
            skillShotBonus = percent.format(Archery.skillShotMaxBonusPercentage);
        }
        else {
            skillShotBonus = percent.format(bonus);
        }

        //DAZE
        String[] dazeStrings = calculateAbilityDisplayValues(Archery.dazeMaxBonusLevel, Archery.dazeMaxBonus);
        dazeChance = dazeStrings[0];
        dazeChanceLucky = dazeStrings[1];

        //RETRIEVE
        String[] retrieveStrings = calculateAbilityDisplayValues(Archery.retrieveMaxBonusLevel, Archery.retrieveMaxChance);
        retrieveChance = retrieveStrings[0];
        retrieveChanceLucky = retrieveStrings[1];
    }

    @Override
    protected void permissionsCheck() {
        canSkillShot = Permissions.bonusDamage(player, skill);
        canDaze = Permissions.daze(player);
        canRetrieve = Permissions.arrowRetrieval(player);
    }

    @Override
    protected boolean effectsHeaderPermissions() {
        return canSkillShot || canDaze || canRetrieve;
    }

    @Override
    protected void effectsDisplay() {
        luckyEffectsDisplay();

        if (canSkillShot) {
            player.sendMessage(LocaleLoader.getString("Effects.Template", LocaleLoader.getString("Archery.Effect.0"), LocaleLoader.getString("Archery.Effect.1")));
        }

        if (canDaze) {
            player.sendMessage(LocaleLoader.getString("Effects.Template", LocaleLoader.getString("Archery.Effect.2"), LocaleLoader.getString("Archery.Effect.3", Archery.dazeModifier)));
        }

        if (canRetrieve) {
            player.sendMessage(LocaleLoader.getString("Effects.Template", LocaleLoader.getString("Archery.Effect.4"), LocaleLoader.getString("Archery.Effect.5")));
        }
    }

    @Override
    protected boolean statsHeaderPermissions() {
        return canSkillShot || canDaze || canRetrieve;
    }

    @Override
    protected void statsDisplay() {
        if (canSkillShot) {
            player.sendMessage(LocaleLoader.getString("Archery.Combat.SkillshotBonus", skillShotBonus));
        }

        if (canDaze) {
            if (isLucky) {
                player.sendMessage(LocaleLoader.getString("Archery.Combat.DazeChance", dazeChance) + LocaleLoader.getString("Perks.lucky.bonus", dazeChanceLucky));
            }
            else {
                player.sendMessage(LocaleLoader.getString("Archery.Combat.DazeChance", dazeChance));
            }
        }

        if (canRetrieve) {
            if (isLucky) {
                player.sendMessage(LocaleLoader.getString("Archery.Combat.RetrieveChance", retrieveChance) + LocaleLoader.getString("Perks.lucky.bonus", retrieveChanceLucky));
            }
            else {
                player.sendMessage(LocaleLoader.getString("Archery.Combat.RetrieveChance", retrieveChance));
            }
        }
    }
}
