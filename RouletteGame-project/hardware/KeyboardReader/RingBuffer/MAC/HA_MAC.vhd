library ieee;
use ieee.std_logic_1164.all;

entity HA_MAC is
	port(
	A, B: in std_logic;	 
	S, Co: out std_logic
	);
end HA_MAC;

architecture structural of HA_MAC is
begin

S <= A xor B;
Co <= A and B;

end structural;