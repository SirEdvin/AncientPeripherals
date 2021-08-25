# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

### Added

- Irrealium tools

### Changed

- Now abstractium artifacts (cutting axe and extracting pickaxe) will consume fuel, to make it more [balanced](https://github.com/SirEdvin/ProgressivePeripherals/issues/19)
- Reworked Recipe registry, now more suitable for [usage](https://github.com/SirEdvin/ProgressivePeripherals/issues/36)
- Recipe registry cooldown was reduced by default

## [0.0.9] - 2021-08-23

### Added

- General endewire network toolkit, wireless sensors, redstone emitter and lamp!
- Ability to wirelessly share peripheral to computer
- Ability to access wirelessly shared peripheral from pocket computer
- Ability to access wirelessly shared peripheral from turtle and ability to share turtle wirelessly in enderwire network!

### Changed

- Flexible Realty anchor and flexible statue now can be [cleaned](https://github.com/SirEdvin/ProgressivePeripherals/issues/30) on crafting table

## [0.0.8] - 2021-08-14

### Fixed

- `lightLevel` support for Flexible reality anchor and Flexible statue [now works fine](https://github.com/SirEdvin/ProgressivePeripherals/issues/33) 

## [0.0.7] - 2021-08-08

### Fixed

- Incompatibility with 0.7r AP, now you should use release version only

## [0.0.6] - 2021-08-08

### Fixed

- Crashes with reality forger [first](https://github.com/SirEdvin/ProgressivePeripherals/issues/27), [second](https://github.com/SirEdvin/ProgressivePeripherals/issues/26) by blacklist introducing
- Flexible statue loot [dup](https://github.com/SirEdvin/ProgressivePeripherals/issues/28)
- Flexible reality anchor loot [dup](https://github.com/SirEdvin/ProgressivePeripherals/issues/25)
- Flexible statue correct [placing](https://github.com/SirEdvin/ProgressivePeripherals/issues/29)
- Flexible reality anchor correct placing

## [0.0.5] - 2021-08-08

### Added

- [Event distributor](https://github.com/SirEdvin/ProgressivePeripherals/issues/13)

### Changed

- Added `lightLevel` support for Flexible statue and Flexible reality anchor

### Fixed

- [SendXPToOwner removed XP from player](https://github.com/SirEdvin/ProgressivePeripherals/issues/24)

## [0.0.4] - 2021-07-30

### Added

- Reality Forger MK2 peripheral
- Creative item duplicator
- Documentation for Recipe Registry and Brewing automata core
- Creative RBT point power generator

### Fixed

- Crash for machinery connect
- Model render update delay

## [0.0.3] - 2021-07-29

### Added

- Knowledgium material
- [Recipe registry](https://github.com/SirEdvin/ProgressivePeripherals/issues/15)
- [Brewing automata core](https://github.com/SirEdvin/ProgressivePeripherals/issues/11)

### Fixed

- JEI integration for Automata recipes
- Problem with `getLevel` abstract

## [0.0.2] - 2021-07-29

### Changed

- XP now store as double, not as integer

### Fixed

- Client-server recipe serialization logic
