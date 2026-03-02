library ieee;
use ieee.std_logic_1164.all;

entity haC is
	port(
	A, B: in std_logic;	 
	S, Co: out std_logic
	);
end haC;

architecture structural of haC is
begin

S <= A xor B;
Co <= A and B;

end structural;