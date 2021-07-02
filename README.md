# LLivesHardcore
**Устройте настоящий хардкор с 1 сердцем и 9 жизнями**

## Скачать
https://github.com/LIMPIX31/LLivesHardcore/releases/

# Документация
## Конфиг


```yml
messagesLang: ru                  # Язык плагина [ru, en]
startLivesCount: 9                # Кол-во жизней даваемых игроку при старте
startPoints: 0                    # Кол-во очков даваемых игроку при старте
healthLimit: 2                    # Сколько у игрока будет HP (2HP = 1 Сердце), 20 - чтобы отключить

reward: 2                         # Награда за ачивку
rewardMultiplier: 2               # На сколько увеличиться награда при преодолении каждых x threshold ачивок
threshold: 50                     # порог ачивок после которых награда увеличиваеться
totemCost: 20                     # Если игрок теряет тотем у него отнимают 20 очков, если у игрока очков не хватает, тотем не сработает (0 - чтобы отключить)

startLivePrice: 100               # Стартовая цена жизни
nextLivePrice: 50                 # + к цене жизни после каждой покупки

minPayment: 1                     # минимальное кол-во очков которое можно передать игроку

actionbarStats: true              # выводить информацию (жизни, очки, ачивки) в экшнбар
subtitleDeathMessage: true        # отображать всем игрокам оповещение о смерти игрока
advancementRewardInChat: true     # выводить в чат сообщение о том сколько очков вы получили за выполнение ачивки

actionBarFormat: "[{\"text\":\"{LIVES}\",\"color\":\"#{LIVES_COLOR}\"},{\"text\":\" | \",\"bold\":true,\"color\":\"dark_gray\"},{\"text\":\"{POINTS}\",\"color\":\"yellow\"},{\"text\":\" | \",\"bold\":true,\"color\":\"dark_gray\"},{\"text\":\"{ADVANCEMENTS}\",\"color\":\"aqua\"}]"
                                  # сообщение выводимое в actionbar

onSpectatorCommands:              # команды которые будут выполенны при последней смерти игркоа
  - "gamemode spectator %player_name%"

pointsTimer:                      # выдавать игроку каждые tx n очков
  enabled: false                  # включить
  timer: "1h" # h - час m - минута s - секунда (tx)
  reward: 15 (n)
```
## Команды
`/llh[1hp] <subcommand>` -  команда плагина, где `<subcommand>` подкоманда плагина
- Право `llh.pluginCommand`

`/llh[1hp]`**Подкоманды**
- `reload[rl]` - перезагрузка конфига
- - Право `llh.reloadConfig`
- `setlives <player> ±<amount>` - установить `<amount>` жизней для игрока `<player>`. Если перед `<amount>` стоит +/- то жизни игрока увеличатся или уменьшатся соответственно
- - Право `llh.setLives`
- `setpoints <player> ±<amount>` - установить `<amount>` жизней для игрока `<player>`. Если перед `<amount>` стоит +/- то жизни игрока увеличатся или уменьшатся соответственно
- - Право `llh.setPoints`
- `setboughtlives <player> ±<amount>` - установить `<amount>` покупок для `<player>`.
- - Право `llh.setBoughtLives`
- `setAdvsc <player> ±<amount>` - установить `<amount>` выполненных достижений для `<player>`.
- - Право `llh.setAdvsc`
- `buylife <player>` - купить жизнь принудительно для игрока `<player>` (Только для NPC)
- - Право `llh.buylifefornpc`

`/buylife[buyl, blife] [?accept]` - позволяет игроку купить жизнь. После выполнения команды у игрока есть 20 секунд, чтобы подтвердить покупку командой `/buylife accept`
- Право `llh.buyLife`
 
`/paypoints[pp] <player> <amount>` - передать игроку `<player>`, `<amount>` очков
- Право `llh.payPoints`

`/getprice` - узнайте сколько стоит купить новую жизнь
- Право `llh.getprice`

## Плейсхолдеры
* `%llh_lives%` - жизни игрока
* `%llh_lives-color%` - жизни игрока (отображаеться цветом от красного до зелёного)
* `%llh_points%` - очки игрока
* `%llh_advsc%` - ачивки игрока
* `%llh_life-price%` - стоимость жизни для игрока
* `%llh_required-points%` - сколько игроку не хватает очков для покупки

## Дополнительно
Чтобы сбросить или перенести данные игроков с одного сервера на другой, удалите или переместите файл **database.db** в папке плагина
