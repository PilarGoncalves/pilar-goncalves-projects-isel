library ieee;
use ieee.std_logic_1164.all;

entity MUX4_1 is
	 port(
	 A, B, C, D : in std_logic;
	 S0, S1 : in std_logic;
	 Y : out std_logic
	 );
end MUX4_1;

architecture structural of MUX4_1 is

begin
 
Y <= not((A and not S1 and not S0) or 
			(B and not S1 and S0) or 
			(C and S1 and not S0) or 
			(D and S1 and S0));

end structural;