# FuckMyDyslexia

## Mod requires
- Cosmic Reach (0.3.4)
- Cosmic Quilt (2.3.1)

## How to test/build
For testing in the dev env, you can use the `gradle run` task

For building, the usual `gradle build` task can be used. The output will be in the `build/libs/` folder

## Notes
- Most project properties can be changed in the `gradle.properties`
- To change the version of Cosmic Loom, edit the `settings.gradle`
- To change author, description and stuff that is not there, edit `src/main/resources/quilt.mod.json`
- The project name is defined in `settings.gradle`
- To add Quilt mods in the build, make sure to use `internal` rather than `implementation`

## License
Mod Licensed under MIT license .