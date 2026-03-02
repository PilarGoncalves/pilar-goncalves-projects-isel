library ieee;
use ieee.std_logic_1164.all;

entity KeyControl is 
	port( 
	Kack : in std_logic;
	Kpress : in std_logic;
   RESET : in std_logic;
   CLK : in std_logic;
   Kval : out std_logic;
	Kscan : out std_logic
	);
end KeyControl;

architecture behavioral of KeyControl is

type STATE_TYPE is (INITIAL_STATE, KEY_PRESSED, WAIT_RELEASE, READY_NEXT);

signal CurrentState, NextState : STATE_TYPE;

begin

CurrentState <= INITIAL_STATE when RESET = '1' else NextState when rising_edge(CLK);

GenerateNextState:
process (CurrentState, Kpress , Kack)
	begin
		case CurrentState is
			when INITIAL_STATE	=> if (Kpress = '1') then 
												NextState <= KEY_PRESSED;
											else 
												NextState <= INITIAL_STATE;
											end if;
											
			when KEY_PRESSED		=> if (Kack = '1') then 
												NextState <= WAIT_RELEASE;
											else 
												NextState <= KEY_PRESSED;
											end if;
											
			when WAIT_RELEASE		=> if (Kpress = '0' and Kack = '0') then 
												NextState <= READY_NEXT;
											else 
												NextState <= WAIT_RELEASE;
											end if;
											
			when READY_NEXT		=> NextState <= INITIAL_STATE;
		end case;
	end process;		

Kscan <= '1' when (CurrentState = INITIAL_STATE) else '0';
Kval <= '1' when (CurrentState = KEY_PRESSED) else '0';

end behavioral;	
 
 
 
 
 