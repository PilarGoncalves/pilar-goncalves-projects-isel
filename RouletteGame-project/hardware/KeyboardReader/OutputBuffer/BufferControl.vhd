library ieee;
use ieee.std_logic_1164.all;

entity BufferControl is 
	port( 
	Load : in std_logic;
	ACK : in std_logic;
   RESET : in std_logic;
   CLK : in std_logic;
   Wreg : out std_logic;
	OBfree : out std_logic;
	Dval : out std_logic
	);
end BufferControl;

architecture behavioral of BufferControl is

type STATE_TYPE is (INITIAL_STATE, LOAD_DATA, VALID_DATA, WAIT_ACK0);

signal CurrentState, NextState : STATE_TYPE;

begin

CurrentState <= INITIAL_STATE when RESET = '1' else NextState when rising_edge(CLK);

GenerateNextState:
process (CurrentState, Load , ACK)
	begin
		case CurrentState is
			when INITIAL_STATE	=> if (Load = '1') then 
												NextState <= LOAD_DATA;
											else 
												NextState <= INITIAL_STATE;
											end if;
											
			when LOAD_DATA		   => if (Load = '0') then 
			                           NextState <= VALID_DATA;
											else
                                    NextState <= LOAD_DATA;
											end if;
											
			when VALID_DATA		=> if (ACK = '1') then 
												NextState <= WAIT_ACK0;
											else 
												NextState <= VALID_DATA;
											end if;
											
			when WAIT_ACK0 		=> if (ACK = '0') then 
												NextState <= INITIAL_STATE;
											else 
												NextState <= WAIT_ACK0;
											end if;
		end case;
	end process;		

OBfree <= '1' when (CurrentState = INITIAL_STATE) else '0';
Wreg <= '1' when (CurrentState = LOAD_DATA) else '0';
Dval <= '1' when (CurrentState = VALID_DATA) else '0';

end behavioral;	
 
 
 
 
 