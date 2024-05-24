-module(userScore).

-export([lerFich/0, parse_lines/1,escreverConta/1]).

%% Function to read the file
lerFich() ->
    {ok, Binary} = file:read_file("contas.txt"),
    Lines = binary:split(Binary, <<"\n">>, [global]),
    parse_lines(Lines).

%% Function to parse the lines
parse_lines(Lines) ->
    lists:foldl(fun(Line, Acc) ->
        case binary:split(Line, <<"/">>, [global]) of
            [User, Password, Score] ->
                maps:put(binary_to_list(User),{binary_to_list(Password), binary_to_integer(Score),false}, Acc);
            _ -> Acc
        end
    end, #{}, Lines).


escreverConta(Users) -> file:write_file("contas.txt", parse(maps:to_list(Users))).

parseUser({User, {Pass, Score, _LoggedIn}}) ->
    string:join([User, Pass, integer_to_list(Score)], "/").

parse(L) ->
    case L of
        [] -> "";
        [H] -> parseUser(H);
        [H | T] -> string:join([parseUser(H), parse(T)], "\n")
    end.
