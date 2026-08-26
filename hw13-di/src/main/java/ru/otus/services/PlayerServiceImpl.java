/*
 * Source: https://github.com/OtusTeam/Java-Pro (Otus Java Pro course materials, lesson L25-di)
 */
package ru.otus.services;

import ru.otus.model.Player;

public class PlayerServiceImpl implements PlayerService {

    private final IOService ioService;

    public PlayerServiceImpl(IOService ioService) {
        this.ioService = ioService;
    }

    @Override
    public Player getPlayer() {
        ioService.out("Представьтесь пожалуйста");
        String playerName = ioService.readLn("Введите имя: ");
        return new Player(playerName);
    }
}
