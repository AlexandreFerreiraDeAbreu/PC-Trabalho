-module(accManager).
-export([login/4, logout/4, criarConta/4, apagarConta/4, autent/3]).


login(User, Pass, FileMap, From) ->
    case maps:find(User, FileMap) of
       {ok, {Password, Score, false }} ->
            if
                Password == Pass ->
                    From ! ok,
                    maps:update(User, {Pass, Score, true}, FileMap);
                true ->
                    From ! pass_incorreta,
                    FileMap
            end;
        _ ->
           From ! conta_invalida,
           FileMap
    end.


logout(User, Pass, FileMap, From) ->
    case maps:find(User, FileMap) of
        {ok, {Password, Score, _}} ->
            if
                Password == Pass ->
                    From ! ok,
                    maps:update(User, {Pass, Score, false}, FileMap);
                true ->
                    From ! pass_incorreta,
                    FileMap
            end;
        _ ->
            From ! conta_invalida,
            FileMap
    end.



criarConta(User, Pass,FileMap,From) ->
    case maps:is_key(User, FileMap) of
        true->
            NewMap = FileMap,
            io:format("Error - User ~p already exists.~n", [User]),
            From ! nome_repetido;
        false ->
            if
                Pass == "" -> 
                    NewMap = FileMap,
                    io:format("Error - Cannot create user with empty password.~n"),
                    From ! password_invalida;
                true ->
                    NewMap = maps:put(User, {Pass, 0, false}, FileMap),
                    io:format("Created an account with username ~p.~n", [User]),
                    userScore:escreverConta(NewMap),
                    From ! ok
            end
    end,
    NewMap.

apagarConta(User, Pass,FileMap,From) ->
    case maps:find(User, FileMap) of
        {ok, {Password, _, _ }} ->
            if
                Password == Pass ->
                    NewMap = maps:remove(User, FileMap),
                    io:format("Removed the account with username ~p.~n", [User]),
                    userScore:escreverConta(NewMap),
                    From ! ok 
            end;
        _ ->
            io:format("Error - Cannot remove user ~p~n.", [User]),
            NewMap = FileMap,
            From ! conta_invalida
    end,
    NewMap.





autent(User, Pass,FileMap) ->
    case maps:find(User, FileMap) of
        {ok, {Password, _ , LoggedIn}} ->
            (Password == Pass) and LoggedIn;
        _ -> false
    end.