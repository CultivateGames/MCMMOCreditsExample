# MCMMOCreditsExample
This is a Minecraft plugin to showcase example API usage for [MCMMOCredits](https://github.com/CultivateGames/MCMMOCredits).

Players who have the "mcmmocredits.example.break" permission, have their credits modified when they break a block defined in config.yml (shown below)

```yml
# Breakable blocks
blocks:
  - STONE
  - SAND
  - DIRT
  - GRASS_BLOCK
# Amount of credits to modify balance
amount: 1
# How to modify the balance of a user when they break the block.
#Options: ADD, SET, TAKE
operation: ADD
```
