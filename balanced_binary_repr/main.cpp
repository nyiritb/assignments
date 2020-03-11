#include <math.h>

bool isBalanced(int n) {
	if (n == 0) {
		return 1;
	}
	unsigned int num = n;
	unsigned int num_len = int(ceil(log2(num)));
	std::cout << num_len << std::endl;
	if (num == 1) {
		num_len = 1;
	}
	unsigned int ones_left = 0;
	unsigned int ones_right = 0;

	int rest = num;

	unsigned int i = 0;
	while (num > 0) {
		i += 1;
		rest = num % 2;
		num = num / 2;
		if (num_len < i * 2 - 1) { 
			if (rest) {
				std::cout << 1;
				ones_left += 1;
			}
			else {
				std::cout << 0;
			}
		}
		else if (num_len > i * 2 - 1) {
			if (rest) {
				std::cout << 1;
				ones_right += 1;
			}
			else {
				std::cout << 0;
			}
		}
		else {
			if (rest) {
				std::cout << '*';
				std::cout << 1;
				std::cout << '*';
			}
			else {
				std::cout << '*';
				std::cout << 0;
				std::cout << '*';
			}
		}
	}
	std::cout << std::endl;
	return ones_left == ones_right;
}

int main()
{
	std::cout << (isBalanced(0)) << std::endl;
	std::cout << std::endl;
	std::cout << (isBalanced(1)) << std::endl;
	std::cout << std::endl;
	std::cout << (isBalanced(2)) << std::endl;
	std::cout << std::endl;
	std::cout << (isBalanced(3)) << std::endl;
	std::cout << std::endl;
	std::cout << (isBalanced(5)) << std::endl;
	std::cout << std::endl;
	std::cout << (isBalanced(6)) << std::endl;
	std::cout << std::endl;
	std::cout << (isBalanced(10)) << std::endl;
	std::cout << std::endl;
	std::cout << (isBalanced(100)) << std::endl;
	std::cout << std::endl;
	std::cout << (isBalanced(-71303458)) << std::endl;
	std::cout << std::endl;
	std::cout << (isBalanced(71434530)) << std::endl;
	std::cout << std::endl;
	std::cout << (isBalanced(2147483647)) << std::endl;
	std::cout << std::endl;
	std::cout << (isBalanced(2147483646)) << std::endl;
	std::cout << std::endl;
	return 0;
}
