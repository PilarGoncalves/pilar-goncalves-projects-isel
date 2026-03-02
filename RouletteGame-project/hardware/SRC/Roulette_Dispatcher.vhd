library ieee;
use ieee.std_logic_1164.all;

entity Roulette_Dispatcher is
	port(
	Dval : in std_logic;
	CLK : in std_logic;
	RESET : in std_logic;
	done : out std_logic;
	WrD : out std_logic;
	Din : in std_logic_vector(7 downto 0);
	Dout : out std_logic_vector(7 downto 0)
	);
end Roulette_Dispatcher;

architecture behavioral of Roulette_Dispatcher is

type STATE_TYPE is (INITIAL_STATE, SEND_STATE, DONE_STATE);

signal Current_State, Next_State : STATE_TYPE;

begin

Current_State <= INITIAL_STATE when RESET = '1' else Next_State when rising_edge(clk);

GenerateNextState:
process (Current_State, Dval)
	begin
		case Current_State is
			when INITIAL_STATE => if (Dval = '1') then
												 Next_State <= SEND_STATE;
												 else 
													  Next_State <= INITIAL_STATE;
												 end if;
			 when SEND_STATE => Next_State <= DONE_STATE;
			 
			 
			 when DONE_STATE => if (Dval = '1') then
												 Next_State <= DONE_STATE;
												 else 
													  Next_State <= INITIAL_STATE;
												 end if;

		end case;
end process;

WrD <= '1' when ( Current_State = SEND_STATE ) else '0';
done <= '1' when ( Current_State = DONE_STATE ) else '0';

Dout <= Din;

end behavioral;