-module(readFile).
-export([lerFich/0, parseFich/2, escreverConta/1, parse/1, parseUser/1]).


lerFich() ->
    io:fwrite(" abrir file \n"),
    Response = file:read_file("contas.txt"),
    case Response of
        {error, _Reason} ->
            io:fwrite(" 1 \n"),
            {ok, NewFile} = file:open("contas.txt", [write]),
            file:close(NewFile),
            #{};
        {ok, File} ->
            io:fwrite(" 2 \n"),
            FileList = string:split(File, "\n"),
            case FileList of
                [<<>>] -> #{};
                _FileList -> parseFich(FileList, #{})
            end
    end.

parseFich([], Users) -> Users;
parseFich([H|T], Users) ->
    [User, UserInfo] = string:split(H, "/"),
    [Pass, Score] = string:split(UserInfo, "/"),
    NewUsers = maps:put(binary_to_list(User), {binary_to_list(Pass), binary_to_integer(Score), false}, Users),
    if
        T == [] -> NewUsers;
        true -> parseFich(string:split(T, "\n"), NewUsers)
    end.

escreverConta(Users) -> file:write_file("users.txt", parse(maps:to_list(Users))).

parseUser({User, {Pass, Score, _LoggedIn}}) ->
    string:join([User, Pass, integer_to_list(Score)], "/").

parse(L) ->
    case L of
        [] -> "";
        [H] -> parseUser(H);
        [H | T] -> string:join([parseUser(H), parse(T)], "\n")
    end.