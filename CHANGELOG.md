## New Features
- Added Void Slot upgrade for Chests, Barrels and Backpacks
    - Allows the destruction of items within the menu screen
    - Crafted using Iron Nuggets, Iron Ingots and Ender Pearl
- Added Item Drum Upgrade
    - Item drums are now defaulted to a max size of 64 stacks (4096 items)
    - Applying the Item Drum Upgrade increases the stack limit by 8, up to a maximum of 256
    - Configurable using the config file (itemDrumDefaultSize, itemDrumMaximumSize, itemDrumUpgradeAmount)
- Change Item Drum interaction to be more consistent
  - Punching now only removes 1 item, shifting and punching removes a whole stack
- Added new statistics and advancements for different block types and all upgrades
## Bug Fixes
- Fixed backpack not opening when holding another backpack in offhand
- Fixed issue with shift clicking items in Chests and Barrels
- Fixed Iron Chests being ignited from lava
- Fixed block / item names not updating until after a client reload when dyeing
- Fixed items leaving Item Drum when mining it
- Fixed missing localization for configuration items
- Rewritten upgrade system which will make additions easier!