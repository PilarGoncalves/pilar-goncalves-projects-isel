library ieee;
use ieee.std_logic_1164.all;

entity DEC2_4 is
	 port(
	 S0, S1 : in std_logic;
	 A, B, C, D : out std_logic
	 );
end DEC2_4;

architecture structural of DEC2_4 is

begin
 
A <= not(not S1 and not S0);
B <= not(not S1 and S0);
C <= not(S1 and not S0);
D <= not(S1 and S0);

end structural;