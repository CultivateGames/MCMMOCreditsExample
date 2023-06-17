# MCMMOCreditsExample
This is a Minecraft plugin to showcase example API usage for [MCMMOCredits](https://github.com/CultivateGames/MCMMOCredits).

API usage is current as of v0.4.1

In this example, players who have the "mcmmocredits.example.break" permission, have their credit balance modified when they break a block in the configured list.

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
