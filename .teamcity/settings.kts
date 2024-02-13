import jetbrains.buildServer.configs.kotlin.*
import jetbrains.buildServer.configs.kotlin.buildSteps.gradle
import jetbrains.buildServer.configs.kotlin.buildSteps.script
import jetbrains.buildServer.configs.kotlin.triggers.vcs
import kotlinx.serialization.json.Json
import java.io.File
import models.SettingsProject
/*
The settings script is an entry point for defining a TeamCity
project hierarchy. The script should contain a single call to the
project() function with a Project instance or an init function as
an argument.

VcsRoots, BuildTypes, Templates, and subprojects can be
registered inside the project using the vcsRoot(), buildType(),
template(), and subProject() methods respectively.

To debug settings scripts in command-line, run the

    mvnDebug org.jetbrains.teamcity:teamcity-configs-maven-plugin:generate

command and attach your debugger to the port 8000.

To debug in IntelliJ Idea, open the 'Maven Projects' tool window (View
-> Tool Windows -> Maven Projects), find the generate task node
(Plugins -> teamcity-configs -> teamcity-configs:generate), the
'Debug' option is available in the context menu for the task.
*/

version = "2023.11"

object MyProject: Project({
    val projects = Json.decodeFromString<SettingsProject>(File("./test.json").toString())

    name = projects.name ?: "Default Name"
    description = projects.description
    buildType(Build)
    buildType(TestFront)
    buildType(BackendTest)
    buildType(DeployQA)
    buildType(DeployProd)
    buildType(Finalize)



    params{
        param("QaDeploy", "false")
    }
    sequential{
        buildType(Build)
        parallel {
            buildType(TestFront)
            buildType(BackendTest)
        }
        if("true" == "%QaDeploy%"){
            buildType(DeployQA)
        }
        else{
            buildType(DeployProd)
        }
        buildType(Finalize)
    }
})

project {
    description = "Test Java pipeline"

    subProject(MyProject)
}

object Build : BuildType({
    name = "Build Test"
    description = "First Build config for java project"

    vcs {
        root(DslContext.settingsRoot)
    }

    steps {
        gradle {
            id = "gradle_runner"
            tasks = "bootJar"
        }
    }
})

object TestFront: BuildType({
    name = "Frontend test"
    vcs {
        root(DslContext.settingsRoot)
    }
    steps{
        script {
            id = "Frontend Test"
            scriptContent = """echo "Frontend has been tested!""""
        }
    }
})

object BackendTest: BuildType({
    name = "Backend test"
    vcs {
        root(DslContext.settingsRoot)
    }
    steps{
        script {
            id = "Backend Test"
            scriptContent = """echo "Backend has been tested!""""
        }
    }
})

object DeployQA: BuildType({
    name = "Deploy to QA"

    vcs {
        root(DslContext.settingsRoot)
    }
    steps{
            script {
                id = "script run"
                scriptContent = """echo "Deployed to QA!""""
            }
    }
})

object DeployProd: BuildType({
    name = "Deploy to Prod"

    vcs {
        root(DslContext.settingsRoot)
    }
    steps{
        script {
            id = "script run"
            scriptContent = """echo "Deployed to Prod!""""
        }
    }
})

object Finalize: BuildType({
    name = "Deploy Notifications"

    vcs {
        root(DslContext.settingsRoot)
    }
    steps{
        script {
            id = "script run"
            scriptContent = """echo "Pipeline finished!""""
        }
    }
    triggers{
        vcs {
        }
    }
})
