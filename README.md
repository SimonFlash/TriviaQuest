# TriviaQuest: In-Game Trivia Questions
TriviaQuest is managed and developed by Simon_Flash. For inquiries, please e-mail me at mcsimonflash@gmail.com

Dislaimer: This plugin has only been public for a short period of time, and as such may contain unexpected bugs. _Use this plugin at your own risk!_

## Important Resources
[Sponge Thread](https://forums.spongepowered.org/t/cmdcalendar-calendar-automatic-command-scheduler-wip-beta/17735)  
[GitHub](https://github.com/SimonFlash/TriviaQuest)

## TriviaQuest 101
TriviaQuest allows players to answer trivia questions and receive rewards for being the first person to answer correctly. TriviaQuest loads pre-configured questions from it's config file, which are randomly selected and broadcasted to the server. The first player to answer the question by typing the answer in chat is declared the winner and received a random reward as specified in the config file, which are done through commands. Furthermore, TriviaQuest also includes an trivia runner to send questions to the chat automatically after a certain interval as specified (once again) in the config.

To help server owners organize and display trivia effectively, questions are segmented into trivia packs that contain groups of questions. These packs not only organize similar questions in the config but can also be enabled and disabled individually without deleting the questions from the config itself.

Note: Players must have the permission `triviaquest.answer` for their answers to be parsed through chat.

## Command Documentation
TriviaQuest's commands are fully documented in-game and accessible using `/Trivia`. For setting up the config, see Configuration.

`/Trivia`: Opens the in-game command documentation  
`/Trivia Answer <Value>`: Answers a trivia question with `Value`  
`/Trivia Post`: Posts a new trivia question to chat  
`/Trivia Start`: Activates the integrated trivia runner*  
`/Trivia Stop`: Halts the integrated trivia runner*  

*Want more control over when TriviaQuest asks questions? Download CmdCalendar [here](https://github.com/SimonFlash/CmdCalendar).

## Configuration
The config file contains three primary sections - config, rewards, and trivia. Each section, along with temporary values, is generated upon installing TriviaQuest. A copy of the default config file can be found below.

```
config {
    # List of enabled Trivia Packs, separated by ", " #
    enabled_packs="pack_A, pack_B"
    # Interval between trivia questions - in seconds #
    trivia_interval=60
    # Length of each trivia question - in seconds #
    trivia_length=30
    # Prefix before TriviaQuest messages - space included #
    trivia_prefix="&8[&5TriviaQuest&8] "
}
rewards {
    # Rewards are commands chosen randomly - use {player} for the players name #
    reward_1="command1"
    reward_2="command2"
}
trivia {
    # Packs should contain related questions and can be enabled/disabled above - names must be unique #
    pack_A {
        # Question names are for your benefit, but must be unique #
        question_1="(Question1),(Answer1)"
        question_2="(Question2),(Answer1),(Answer2)"
    }
    pack_B {
        question_3="(Question3),(Answer1)"
        question_4="(Question4),(Answer1),(Answer2)"
    }
}
```

Note: The config is JSON based, and as such follows JSON formatting. Attempting to use certain characters in the question or answer, such as the doublequote ("), could cause issues with TriviaQuest. Instead, use the backslash (\) to escape characters.

## Upcoming Features
This section contains a list of planned upcoming features for a future version of CmdCalendar. If you would like to suggest a feature, visit the CmdCalendar Sponge thread (above) or send me an e-mail (also above). Asking for features will be added in order of importance and simplicity, and pushing for a new release will not help your cause. These releases are only planned; there is no guarantee what version they will be implemented in.

+ User created questions
+ Player scoreboard tracking
+ Advanced answer control (will require a leading term in answers)

## Issue Reports
This section contains a list of all known bugs and issues in the most recent version of TriviaQuest. Bugs are non-intended errors that exist in the code, while issues are features that do not exist. A feature intended to be implemented into future versions is suffixed [Soon™]. Features will be added in order of importance and simplicity, and pushing for a new release will not help your cause.

+ No issues are currently known! How long will that last...
