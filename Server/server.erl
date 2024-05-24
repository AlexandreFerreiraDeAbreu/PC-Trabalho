-module(server).
-import(userScore, [lerFich/0, escreverConta/1]).
-import(accManager, [login/4, logout/4, criarConta/4, apagarConta/4, autent/3]).
-export([start/1, stop/0]).

start(Port) -> register(?MODULE,spawn(fun() -> server(Port) end)).
stop() -> ?MODULE ! stop.

server(Port) ->
    {ok, LSock} = gen_tcp:listen(Port, [binary, {packet, line}, {reuseaddr, true}]),
    Room = spawn(fun()->room(#{}) end),
    spawn(fun() -> acceptor(LSock) end),
    UsersMap = userScore:lerFich(),
    io:format("Map: ~p~n ",[UsersMap]),
    %loop(readFile:lerFich(),Room),
    loop(UsersMap,Room),
    receive
        stop -> ok
    end.


loop(UsersMap,Room)->
    receive
        {criarConta, User, Pass, From} ->
            NewUsersMap = accManager:criarConta(User, Pass, UsersMap, From),
            loop(NewUsersMap, Room);
        {apagarConta, User, Pass, From} ->
            NewUsersMap = accManager:apagarConta(User, Pass, UsersMap, From),
            loop(NewUsersMap, Room);
        {login, User, Pass, From} ->
            io:fwrite("loop login"),
            NewUsersMap = accManager:login(User, Pass, UsersMap, From),
            loop(NewUsersMap, Room);
        {logout, User, Pass, From} ->
            NewUsersMap = accManager:logout(User, Pass, UsersMap, From),
            loop(NewUsersMap, Room);
        {leaderboard, From} ->
            UserList = [{Username, Score} || {Username, {_Passw, Score, _LoggedIn}} <- maps:to_list(UsersMap)],
            From ! UserList,
            loop(UsersMap,Room);
        {enter, User, From} ->
            Room ! {enterRoom, User, From},
            loop(Room, UsersMap)
    end.


    
acceptor(LSock) ->
    Res = gen_tcp:accept(LSock),
    %spawn(fun() -> acceptor(LSock) end),
    case Res of
        {ok, Sock} ->
            spawn(fun() -> acceptor(LSock) end),
            client(Sock);
        {error, Reason} -> io:fwrite("Error - Can't connect to tcp.\n"),Reason
    end.

room(Queue) ->
    receive
         {enterRoom, User, FromPid} ->
            NewQueue = Queue ++ [{User, FromPid}],
            case length(NewQueue) of
                2 ->
                    io:fwrite("Começar partida");  %match(?MODULE, NewQueue); %spawn?
                _ ->
                    room(NewQueue)
            end;
        {leave, User, From} ->
            io:format("leave received in lobby ~p~n", [User])
            % From ! left
    end.

client(Sock)->
    receive
        {tcp, _, Data} ->
            % Lobby ! {line, Data},
            inputFromClient(Data, Sock),
            client(Sock)
    end.

inputFromClient(Data,Sock)->
    io:fwrite("handle client input"),
    String = binary_to_list(string:trim(Data, trailing,"\n")),
    case string:split(String, "/") of
        ["login", Msg] ->
            io:fwrite("handle client input -> Login"),
            [User, Pass] = string:split(Msg, "/"),
            ?MODULE ! {login,User,Pass,self()},
            receive
                ok -> gen_tcp:send(Sock, "ok\n"), io:fwrite("ok\n");
                conta_invalida -> gen_tcp:send(Sock, "conta_invalida\n"),io:fwrite("containv");
                pass_incorreta ->gen_tcp:send(Sock, "pass_incorreta\n"),io:fwrite("passinc")
            end;
        ["logout", Msg] ->
            [User, Pass] = string:split(Msg, "/"),
            ?MODULE ! {logout,User,Pass,self()},
            receive
                ok -> gen_tcp:send(Sock, "ok\n");
                conta_invalida -> gen_tcp:send(Sock, "conta_invalida\n");
                pass_incorreta ->gen_tcp:send(Sock, "pass_incorreta\n")
            end;
        ["criarConta", Msg] ->
            [User, Pass] = string:split(Msg, "/"),
            ?MODULE ! {criarConta,User,Pass,self()},
            receive
                ok -> gen_tcp:send(Sock, "ok\n");
                nome_repetido -> gen_tcp:send(Sock, "nome_repetido\n");
                password_invalida -> gen_tcp:send(Sock, "password_invalida\n")
            end;
        ["apagarConta", Msg] ->
            [User, Pass] = string:split(Msg, "/"),
            ?MODULE ! {apagarConta,User,Pass,self()},
            receive
                ok -> gen_tcp:send(Sock, "ok\n");
                conta_invalida -> gen_tcp:send(Sock, "conta_invalida\n")
                % um para pass invalida?
            end;
        ["leaderboard", _] ->
            ?MODULE ! {leaderboard,self()},
            receive
                Users -> 
                    UserList = [string:join([Username, integer_to_list(Score)], " ") || {Username, Score} <- Users],
                    Res = string:join(UserList, "|"),
                    io:format("~w ~p~n", [length(UserList), Res]),
                   gen_tcp:send(Sock, string:join([Res, "\n"], ""))
            end;
        ["enter", Username] ->
            ?MODULE ! {play, Username, self()},
            receive
                {done, MatchPid} ->
                    gen_tcp:send(Sock, "join:done\n")
                    %clientMatchLoop(Sock, MatchPid, Username)
            end;
        _ -> io:fwrite("Pedido desconhcido.\n")
    end.



