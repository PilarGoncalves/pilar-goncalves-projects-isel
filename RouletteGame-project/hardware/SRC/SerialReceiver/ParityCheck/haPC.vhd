library ieee;
use ieee.std_logic_1164.all;

entity haPC is
	port(
	A, B: in std_logic;	 
	S, Co: out std_logic
	);
end haPC;

architecture structural of haPC is
begin

S <= A xor B;
Co <= A and B;

end structural;